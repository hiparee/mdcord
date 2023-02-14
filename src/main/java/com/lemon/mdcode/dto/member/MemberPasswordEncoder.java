package com.lemon.mdcode.dto.member;

public interface MemberPasswordEncoder {

    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);

}
