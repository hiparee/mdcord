package com.lemon.mdcode.domain.chat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "channel_chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_chat_id")
    private Integer id;

    @Column(name = "channel_id")
    private Integer channelId;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "content")
    private String content;

    @Column(name = "file_yn")
    private String fileYn;

    @Column(name = "fix_yn")
    private String fixYn;

    @Column(name = "reply_chat_id")
    private String replyChatId;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "create_by")
    private String createBy;

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
