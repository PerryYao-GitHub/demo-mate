package com.ypy.matebackend.common;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN((byte) 0),
    USER((byte) 1);

    private final Byte code;

    UserRole(Byte code) {
        this.code = code;
    }

}
