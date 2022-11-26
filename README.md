## 보상지급 API 과제

매일 00시 00분 00초에 선착순 10명에게 보상 지급 하는 RESTAPI


### 빌드 & 실행

---

```shell
git clone https://github.com/BlackGrammer/engagement.git
cd engagement
./gradlew build
cd ./build/libs
java -jar engagement.jar
```


<br/><br/>
### 사용기술

---

* Java
* Spring Boot
* Spring Data JPA
* Spring Security
* Redisson
* QueryDsl


<br/><br/>
### 패키지 구조

---

```
engagement
    ├── app
    │     ├── event (보상)
    │     │     ├── application
    │     │     │     ├── dto 
    │     │     │     ├── exception
    │     │     │     └── mapper
    │     │     │ 
    │     │     ├── domain
    │     │     │     ├── entity
    │     │     │     ├── enums
    │     │     │     ├── repository
    │     │     │     └── strategy (보상전략)
    │     │     │ 
    │     │     ├── infrastructure
    │     │     │     └── converter
    │     │     │
    │     │     └── presentation
    │     │
    │     └── member (유저)
    │         ├── application
    │         │     └── dto
    │         │
    │         ├── domain
    │         │     ├── entity
    │         │     └── repository
    │         │
    │         └── presentation
    │
    └── core  (전역설정 및 유틸)
        ├── aspect
        ├── config
        ├── exception
        ├── model
        └── util
```

<br/><br/>
### API

---

### 보상 데이터 조회

<code>GET /api/events/{eventId}</code>

**경로**

| 필드      | 타입     | 설명       | 필수  |
|:--------|:-------|:---------|:---:|
| eventId | String | 보상이벤트 ID |  Y  |

**사용예시**

```
curl -X GET http://userHost:8080/api/events/reward
```

**응답예시**

```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "id": "reward",
    "title": "매일 00시 00분 00초 선착순 10명 100 포인트 지급!!!",
    "description": "• 보상지급방식은사용자가받기를누를때지급하게 됩니다......",
    "eventType": "CONT",
    "dailyLimit": 10
  }
}
```


<br/><br/>
---

### 보상 지급내역 조회

<code>GET /api/events/{eventId}/rewards?date={date}&direction={direction}</code>

**경로**

| 필드        | 타입     | 설명                             | 필수  |
|:----------|:-------|:-------------------------------|:---:|
| eventId   | String | 보상이벤트 ID (Sample: 'reward')    |  Y  |

**요청변수**

| 필드        | 타입     | 설명                             | 필수  |
|:----------|:-------|:-------------------------------|:---:|
| date      | String | yyyy-mm-dd 형식 문자열              |  Y  |
| direction | String | 오름차순:ASC, 내림차순: DESC (기본값 ASC) |  N  |

**사용예시**

```
curl -X GET http://userHost:8080/api/events/reward/rewards?date=2022-11-23?direction=ASC
```

**응답예시**

```json
{
  "code": 200,
  "message": "OK",
  "data": [
    {
      "id": 21,
      "eventTitle": "매일 00시 00분 00초 선착순 10명 100 포인트 지급!!!",
      "memberId": 6,
      "memberName": "아이언맨",
      "point": 1000,
      "seq": 3,
      "issuedAt": "2022-11-23T00:00:01"
    },
    {
      "id": 23,
      "eventTitle": "매일 00시 00분 00초 선착순 10명 100 포인트 지급!!!",
      "memberId": 8,
      "memberName": "헐크",
      "point": 1000,
      "seq": 3,
      "issuedAt": "2022-11-23T00:00:03"
    },
    ....
  ],
  "pagination": {
    "totalElements": 10,
    "numberOfElements": 10,
    "totalPages": 1,
    "number": 0,
    "size": 10,
    "hasNext": false
  }
}
```


<br/><br/>
---

### 보상지급 요청

```
POST /api/events/{eventId}/rewards
Header Authorization: Bearer {accessToken}
```

**경로**

| 필드      | 타입     | 설명                             | 필수  |
|:--------|:-------|:-------------------------------|:---:|
| eventId | String | 보상이벤트 ID (Sample: 'reward')    |  Y  |

**헤더**

| 필드          | 타입     | 설명              | 필수  |
|:------------|:-------|:----------------|:---:|
| accessToken | String | 유저 access token |  Y  |
> 엑세스 토큰 발급은 아래 [유저 인증 API](#유저-인증) 에서 가능합니다. 

**사용예시**

```
curl -X POST localhost:8080/api/events/reward/rewards \
-H "Authorization: Bearer {your access Token}"
```

**응답예시 - 지급성공**

```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "point": 1000,
    "seq": 1,
    "eventTitle": "매일 00시 00분 00초 선착순 10명 100 포인트 지급!!!",
    "eventType": "CONT"
  }
}
```

**응답예시 - 지급실패 (같은 유저가 동일한날 보상 지급 재요청)**

```json
{
  "code": "ERR-EVT-403-002",
  "message": "이미 보상지급이 완료되었습니다."
}
```

**응답예시 - 지급실패 (마감된 보상에 대해 지급 요청)**

```json
{
  "code": "ERR-EVT-403-001",
  "message": "오늘의 보상지급이 마감되었습니다. 내일 다시 도전해보세요."
}
```


<br/><br/>
---

### 유저 인증

```
POST /api/members/
{
  "memberName" : {memberName}
}
```

**Body**

| 필드         | 타입     | 설명  | 필수  |
|:-----------|:-------|:----|:---:|
| memberName | String | 유저명 |  Y  |
> 프로젝트 시작시 15명의 샘플 유저가 등록되어 있습니다.
> 
> [ 짱구 ,철수 ,유리 ,맹구 ,훈이 ,아이언맨 ,토르 ,헐크 ,캡틴 ,호크아이 ,피카츄 ,이상해씨 ,파이리 ,꼬부기 ,잠만보 ]


**사용예시**

```
curl -X POST localhost:8080/api/members/authorize \
-H "Content-Type: application/json" \
-d '{"memberName":"짱구" }'
```

**응답예시**

```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "accessToken": "eyJ0eXAiOiJK...."
  }
}
```


