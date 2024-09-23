package com.ypy.matebackend.common;

import lombok.Getter;

@Getter
public enum UserStatus {
    BANNED((byte) 0),
    AVAILABLE((byte) 1);

    private final Byte code;

    UserStatus(Byte code) {
        this.code = code;
    }
}
