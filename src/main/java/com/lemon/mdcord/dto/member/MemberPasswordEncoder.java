package com.lemon.mdcord.dto.member;

public interface MemberPasswordEncoder {

    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);

}
