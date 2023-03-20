package com.lemon.mdcord.domain.chat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.lemon.mdcord.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "attach_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attach_file_id")
    private Long id;

    @NotBlank
    @Column(name = "origin_file_name", nullable = false, length = 255)
    private String originFileName;

    @NotBlank
    @Column(name = "real_file_name", nullable = false, length = 255)
    private String realFileName;

    @NotBlank
    @Column(name = "file_ext", nullable = false, length = 10)
    private String fileExt;

    @NotNull
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_chat_id")
    private ChannelChat channelChat;

    @Builder
    public AttachFile(String originFileName, String realFileName, String fileExt, Long fileSize, ChannelChat channelChat, String createBy) {
        this.originFileName = originFileName;
        this.realFileName = realFileName;
        this.fileExt = fileExt;
        this.fileSize = fileSize;
        this.channelChat = channelChat;
        super.setCreateBy(createBy);
    }
}
