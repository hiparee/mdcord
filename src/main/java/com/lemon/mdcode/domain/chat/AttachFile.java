package com.lemon.mdcode.domain.chat;

import com.lemon.mdcode.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "attach_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attach_file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_chat_id")
    private ChannelChat channelChat;

    @Column(name = "origin_file_name")
    private String originFileName;

    @Column(name = "real_file_name")
    private String realFileName;

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "file_size")
    private Integer fileSize;

}
