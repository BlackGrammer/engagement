package kr.co.engagement.app.event.application.dto;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardSearchCondition {

    @NotNull(message = "날짜는 필수 입력값입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private Sort.Direction direction;

    public Sort.Direction getDirection() {
        return direction == null ? Sort.Direction.ASC : direction;
    }
}
