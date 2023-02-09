package com.lemon.mdcord.domain.member;

import lombok.Getter;

@Getter
public enum MemberRole {

    ADMIN("ADMIN"),
    USER("USER");

    private final String value;

    MemberRole(String value) {
        this.value = value;
    }

}
