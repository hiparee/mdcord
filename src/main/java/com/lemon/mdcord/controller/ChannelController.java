package com.lemon.mdcord.controller;

import com.lemon.mdcord.dto.channel.list.ChannelListOrderUpdateRequest;
import com.lemon.mdcord.dto.channel.list.*;
import com.lemon.mdcord.service.channel.ChannelListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @Operation(summary = "채널 수정", description = "채널 수정 API")
    @PutMapping("/channels")
    public ChannelListUpdateResponse updateChannelList(@RequestBody @Valid ChannelListUpdateRequest dto) {
        return new ChannelListUpdateResponse(channelListService.updateChannelInfo(dto));
    }

    @Operation(summary = "채널 순서 변경", description = "채널 순서 변경 API")
    @PutMapping("/channels/order")
    public void updateChannelOrder(@RequestBody List<ChannelListOrderUpdateRequest> list) {
        channelListService.updateChannelOrder(list);
    }

}
