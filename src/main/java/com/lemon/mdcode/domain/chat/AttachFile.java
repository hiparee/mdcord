package com.lemon.mdcode.domain.chat;

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
public class AttachFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attach_file_id")
    private Integer id;

    @Column(name = "channel_chat_id")
    private Integer channelChatId;

    @Column(name = "origin_file_name")
    private String originFileName;

    @Column(name = "real_file_name")
    private String realFileName;

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "file_size")
    private Integer fileSize;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "create_by")
    private String createBy;

}
