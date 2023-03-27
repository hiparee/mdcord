package com.lemon.mdcord.service.member;

import com.lemon.mdcord.domain.member.Member;
import com.lemon.mdcord.dto.member.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface MemberService {

    MemberCreateResponse createMember(MemberCreateRequest dto);

    MemberLoginResponse memberLogin(MemberLoginRequest dto, HttpServletResponse response);

    MemberUpdateResponse updateUser(String memberId, MemberUpdateRequest dto);

    List<Member> getMemberList();

    void memberLogout(HttpServletRequest request, HttpServletResponse response);
}
