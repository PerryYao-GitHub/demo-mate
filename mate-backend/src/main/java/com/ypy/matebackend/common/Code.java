package com.ypy.matebackend.common;

public enum Code {
    SUCCESS(200, "success"),
    ERROR_PARAMS_NULL(400, "error: null params"),
    ERROR_PARAMS_INVALID(401, "error: invalid params"),
    ERROR_AUTH(403, "error: authentication failed"),
    ERROR_BACKEND(500, "error: backend");

    private final int code;
    private final String msg;

    Code(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
