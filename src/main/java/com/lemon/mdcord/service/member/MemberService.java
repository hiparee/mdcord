package com.lemon.mdcord.service.member;

import com.lemon.mdcord.domain.member.Member;
import com.lemon.mdcord.dto.member.MemberCreateRequest;
import com.lemon.mdcord.dto.member.MemberLoginRequest;
import com.lemon.mdcord.dto.member.MemberUpdateRequest;

public interface MemberService {

    Member createMember(MemberCreateRequest dto);

    Member memberLogin(MemberLoginRequest dto);

    Member updateUser(MemberUpdateRequest dto);

}
