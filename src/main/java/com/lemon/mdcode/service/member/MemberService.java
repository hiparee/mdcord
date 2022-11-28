package com.lemon.mdcode.service.member;

import com.lemon.mdcode.domain.member.Member;
import com.lemon.mdcode.dto.member.MemberCreateRequest;
import com.lemon.mdcode.dto.member.MemberLoginRequest;
import com.lemon.mdcode.dto.member.MemberUpdateRequest;

public interface MemberService {

    Member createMember(MemberCreateRequest dto);

    Member memberLogin(MemberLoginRequest dto);

    Member updateUser(MemberUpdateRequest dto);

}
