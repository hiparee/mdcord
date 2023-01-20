package com.lemon.mdcord.repository;

import com.lemon.mdcord.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findMemberById(String memberId);

    Optional<Member> findMemberByIdAndUseYn(String memberId, String useYn);

}
