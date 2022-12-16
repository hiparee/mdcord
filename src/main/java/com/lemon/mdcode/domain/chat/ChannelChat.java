package com.lemon.mdcode.domain.chat;

import com.lemon.mdcode.domain.BaseEntity;
import com.lemon.mdcode.domain.channel.ChannelList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DynamicInsert
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

    @Column(name = "file_yn")
    @ColumnDefault("'N'")
    private String fileYn;

    @Column(name = "fix_yn")
    @ColumnDefault("'N'")
    private String fixYn;

    @Column(name = "reply_chat_id")
    private Long replyChatId;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "delete_yn")
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
