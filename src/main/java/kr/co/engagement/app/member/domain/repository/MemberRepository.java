package kr.co.engagement.app.member.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.engagement.app.member.domain.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberName(String memberName);
}
