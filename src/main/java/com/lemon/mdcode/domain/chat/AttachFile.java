package com.lemon.mdcode.domain.chat;

import com.lemon.mdcode.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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
    @Column(name = "origin_file_name", nullable = false)
    private String originFileName;

    @NotBlank
    @Column(name = "real_file_name", nullable = false)
    private String realFileName;

    @NotBlank
    @Column(name = "file_ext", nullable = false)
    private String fileExt;

    @NotBlank
    @Column(name = "file_size", nullable = false)
    private Integer fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_chat_id")
    private ChannelChat channelChat;

}
