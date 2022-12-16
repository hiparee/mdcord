package com.lemon.mdcode.domain.chat;

import com.lemon.mdcode.domain.BaseEntity;
import com.lemon.mdcode.domain.channel.ChannelList;
import com.lemon.mdcode.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "channel_chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelChat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private ChannelList channelList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "content")
    private String content;

    @Column(name = "file_yn")
    private String fileYn;

    @OneToMany(mappedBy = "channelChat")
    private List<AttachFile> attachFiles = new ArrayList<>();

    @Column(name = "fix_yn")
    private String fixYn;

    @Column(name = "reply_chat_id")
    private String replyChatId;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "delete_yn")
    private String deleteYn;

    @Column(name = "delete_by")
    private String deleteBy;

}
