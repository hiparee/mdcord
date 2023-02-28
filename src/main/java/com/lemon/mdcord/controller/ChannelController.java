package com.lemon.mdcord.controller;

import com.lemon.mdcord.dto.channel.ChannelListCreateRequest;
import com.lemon.mdcord.dto.channel.ChannelListCreateResponse;
import com.lemon.mdcord.dto.channel.MultipleChannelListResponse;
import com.lemon.mdcord.service.channel.ChannelListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Channel Controller")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelListService channelListService;

    @Operation(summary = "채널 등록", description = "채널 등록 API")
    @PostMapping("/channels")
    public ChannelListCreateResponse createChannelList(@RequestBody @Valid final ChannelListCreateRequest dto) {
        return new ChannelListCreateResponse(channelListService.createChannel(dto));
    }

    @Operation(summary = "채널 목록 조회", description = "채널 목록 조회 API")
    @GetMapping("/channels")
    public MultipleChannelListResponse fetchChannelList() {
        return channelListService.fetchChannels();
    }

    @Operation(summary = "채널 삭제", description = "채널 삭제 API. 사용 여부(use_yn) Y -> N")
    @DeleteMapping("/channels/{id}")
    public void deleteChannelList(@RequestParam("id") Long id) {
        channelListService.deleteChannel(id);
    }

    // 수정 기능 (order, 채널명, useYn) useyn n->y 때문에 필요
    //  - 수정된 값 return
}
