package com.lemon.mdcode.dto.channel;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChannelResponse {

    private Integer channelId;

    private Integer parentId;

    private String channelName;

    private String useYn;

    private Integer dept;

    private LocalDateTime createDate;

    private String createBy;

    private LocalDateTime updateDate;

    private String updateBy;

}
