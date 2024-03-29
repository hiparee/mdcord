package com.lemon.mdcord.domain.channel;

import com.lemon.mdcord.domain.BaseEntity;
import com.lemon.mdcord.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "channel_member")
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private ChannelList channelList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "state", nullable = false, length = 3)
    @ColumnDefault("'OFF'")
    private String state;

    @Builder
    public ChannelMember(ChannelList channelList, Member member, String createBy) {
        this.channelList = channelList;
        this.member = member;
        super.setCreateBy(createBy);
    }
}
