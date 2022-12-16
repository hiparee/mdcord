package com.lemon.mdcode.controller;

import com.lemon.mdcode.dto.member.*;
import com.lemon.mdcode.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/signup")
    public MemberCreateResponse createMember(@RequestBody @Valid final MemberCreateRequest dto) {
        return new MemberCreateResponse(memberService.createMember(dto));
    }

    @PostMapping("/members/signin")
    public MemberLoginResponse signin(@RequestBody final MemberLoginRequest dto) {
        return new MemberLoginResponse(memberService.memberLogin(dto));
    }

    @PutMapping("/member")
    public MemberUpdateResponse updateMember(@RequestBody final MemberUpdateRequest dto) {
        return new MemberUpdateResponse(memberService.updateUser(dto));
    }

}
