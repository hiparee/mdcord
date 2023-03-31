package com.lemon.mdcord.controller;

import com.lemon.mdcord.dto.member.*;
import com.lemon.mdcord.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Member Controller")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "사용자 정보 등록", description = "사용자 정보 등록 API")
    @PostMapping("/members/signup")
    public MemberCreateResponse createMember(@RequestBody @Valid final MemberCreateRequest dto) {
        return memberService.createMember(dto);
    }

    @Operation(summary = "로그인", description = "로그인 API")
    @PostMapping("/members/signin")
    public MemberLoginResponse signIn(@RequestBody @Valid final MemberLoginRequest dto, HttpServletResponse response) {
        return memberService.memberLogin(dto, response);
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정 API")
    @PutMapping("/members/{memberId}")
    public MemberUpdateResponse updateMember(
            @PathVariable("memberId") @Size(max = 20) String memberId
            , @RequestBody @Valid final MemberUpdateRequest dto) {
        return memberService.updateUser(memberId, dto);
    }

    @Operation(summary = "사용자 목록 조회", description = "사용자 목록 조회 API")
    @GetMapping("/members")
    public List<MemberListResponse> getMemberList() {
        return memberService.getMemberList().stream()
                .map(MemberListResponse::new)
                .sorted(Comparator.comparing(MemberListResponse::getCreateDate).reversed())
                .collect(Collectors.toList());
    }

    @Operation(summary = "로그아웃", description = "로그아웃 API")
    @PostMapping("/members/signout")
    public ResponseEntity<Void> signOut(HttpServletRequest request, HttpServletResponse response) {
        memberService.memberLogout(request, response);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(null);
    }

}
