package com.lemon.mdcord.controller;

import com.lemon.mdcord.dto.channel.member.ChannelMemberCreateRequest;
import com.lemon.mdcord.dto.channel.member.ChannelMemberCreateReseponse;
import com.lemon.mdcord.service.channel.ChannelMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "ChannelMember Controller")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChannelMemberController {

    /*
        TODO -
        1. 관리자는 채널이 생성되면 무조건 등록
        2. 일반 유저는 어떻게 초대? 채널 입장?
            -> 관리자만 할 수 있게
     */

    private final ChannelMemberService channelMemberService;


    @Operation(summary = "채널 멤버 등록", description = "채널 멤버 등록 API")
    @PostMapping("/channel-member")
    public ChannelMemberCreateReseponse createChannelList(@RequestBody @Valid final ChannelMemberCreateRequest dto) {
        return new ChannelMemberCreateReseponse(channelMemberService.createChannelMember(dto));
    }

//    @Operation(summary = "멤버 소속 채널 조회", description = "멤버 소속 채널 조회 API")
//    @GetMapping("/channel-member")
//    public ChannelMemberCreateReseponse getMemberChannelList(@RequestBody @Valid final ChannelMemberCreateRequest dto) {
//        return new ChannelMemberCreateReseponse(channelMemberService.createChannelMember(dto));
//    }

}
