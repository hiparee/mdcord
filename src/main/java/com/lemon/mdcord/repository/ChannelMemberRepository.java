package com.lemon.mdcord.repository;

import com.lemon.mdcord.domain.channel.ChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {

    List<ChannelMember> findByMemberId(String memberId);

}
