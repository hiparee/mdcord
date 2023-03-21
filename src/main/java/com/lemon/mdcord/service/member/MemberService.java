package com.lemon.mdcord.service.member;

import com.lemon.mdcord.domain.member.Member;
import com.lemon.mdcord.dto.member.MemberCreateRequest;
import com.lemon.mdcord.dto.member.MemberLoginRequest;
import com.lemon.mdcord.dto.member.MemberUpdateRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface MemberService {

    Member createMember(MemberCreateRequest dto);

    Member memberLogin(MemberLoginRequest dto, HttpServletResponse response);

    Member updateUser(MemberUpdateRequest dto);

    List<Member> getMemberList();
}
