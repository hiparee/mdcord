package com.lemon.mdcord.controller;

import com.lemon.mdcord.dto.member.*;
import com.lemon.mdcord.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "Member Controller")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "사용자 정보 등록", description = "사용자 정보 등록 API")
    @PostMapping("/members/signup")
    public MemberCreateResponse createMember(@RequestBody @Valid final MemberCreateRequest dto) {
        return new MemberCreateResponse(memberService.createMember(dto));
    }

    @Operation(summary = "로그인", description = "로그인 API")
    @PostMapping("/members/signin")
    public MemberLoginResponse signin(@RequestBody final MemberLoginRequest dto, HttpServletResponse response) {
        return new MemberLoginResponse(memberService.memberLogin(dto, response));
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정 API")
    @PutMapping("/members")
    public MemberUpdateResponse updateMember(@RequestBody final MemberUpdateRequest dto) {
        return new MemberUpdateResponse(memberService.updateUser(dto));
    }

    @Operation(summary = "사용자 목록 조회", description = "사용자 목록 조회 API")
    @GetMapping("/members")
    public Page<MemberListResponse> getMemberList(@PageableDefault(size = 20) Pageable pageable) {
        return memberService.getMemberList(pageable);
    }

}
