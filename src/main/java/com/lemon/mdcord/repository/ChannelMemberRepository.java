package com.lemon.mdcord.repository;

import com.lemon.mdcord.domain.channel.ChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {

    List<ChannelMember> findByMemberId(String memberId);
    List<ChannelMember> findByChannelListIdIn(List<Long> joinedChannelIds);

    @Modifying
    @Query("update ChannelMember cm set cm.state = 'OFF'")
    void changeAllStateOff();

    @Modifying
    @Query("update ChannelMember cm set cm.state = :state where cm.member.id = :memberId")
    void changeMemberState(@Param("memberId") String memberId, @Param("state") String state);
}
