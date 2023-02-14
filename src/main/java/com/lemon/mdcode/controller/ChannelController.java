package com.lemon.mdcode.controller;

import com.lemon.mdcode.dto.channel.ChannelListCreateRequest;
import com.lemon.mdcode.dto.channel.ChannelListCreateResponse;
import com.lemon.mdcode.dto.channel.MultipleChannelListResponse;
import com.lemon.mdcode.service.channel.ChannelListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelListService channelListService;

    @PostMapping("/channel-list")
    public ChannelListCreateResponse createChannelList(@RequestBody @Valid final ChannelListCreateRequest dto) {
        return new ChannelListCreateResponse(channelListService.createChannelList(dto));
    }

    @GetMapping("/channel-list")
    public MultipleChannelListResponse fetchChannelList() {
        return channelListService.fetchChannelList();
    }

}
