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
    @PostMapping("/channel-list")
    public ChannelListCreateResponse createChannelList(@RequestBody @Valid final ChannelListCreateRequest dto) {
        return new ChannelListCreateResponse(channelListService.createChannelList(dto));
    }

    @Operation(summary = "채널 조회", description = "채널 조회 API")
    @GetMapping("/channel-list")
    public MultipleChannelListResponse fetchChannelList() {
        return channelListService.fetchChannelList();
    }

}
