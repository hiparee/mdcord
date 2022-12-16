package com.lemon.mdcode.domain.chat;

import com.lemon.mdcode.domain.BaseEntity;
import com.lemon.mdcode.domain.channel.ChannelList;
import com.lemon.mdcode.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    @NotBlank
    @Column(name = "content", nullable = false)
    private String content;

    @NotBlank
    @Column(name = "file_yn", nullable = false)
    @ColumnDefault("'N'")
    private String fileYn;

    @NotBlank
    @Column(name = "fix_yn", nullable = false)
    @ColumnDefault("'N'")
    private String fixYn;

    @Column(name = "reply_chat_id")
    private Long replyChatId;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @NotBlank
    @Column(name = "delete_yn", nullable = false)
    @ColumnDefault("'N'")
    private String deleteYn;

    @Column(name = "delete_by")
    private String deleteBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private ChannelList channelList;

    @OneToMany(mappedBy = "channelChat")
    private List<AttachFile> attachFiles = new ArrayList<>();

}
