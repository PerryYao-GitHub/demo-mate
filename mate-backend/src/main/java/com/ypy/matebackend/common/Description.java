package com.ypy.matebackend.common;

public enum Description {
    REGISTER_SUCCESS("register success", "注册成功"),
    LOGIN_SUCCESS("login success", "登陆成功"),
    CHANGE_PASSWORD_SUCCESS("change password success", "修改密码成功"),
    DEL_ACCOUNT_SUCCESS("delete account success", "销户成功"),
    EDIT_ACCOUNT_SUCCESS("edit success", "编辑成功"),
    WRONG_ACCOUNT_PASSWORD("wrong account or password", "账户密码错误"),
    WRONG_CONFIRM_PASSWORD("wrong confirm password", "确认密码错误"),
    WRONG_ACCOUNT_FORMAT("account can only contain 0 - 9, a - z, A - Z and _", "账户仅包含数字、字母和下划线"),
    ACCOUNT_EXISTS("account has existed", "账户已被使用"),
    EMPTY_ERROR("account, password, confirm password can't be empty", "账号密码不能为空");

    private static final String LANG = "zh";

    private final String en;
    private final String zh;

    Description(String en, String zh) {
        this.en = en;
        this.zh = zh;
    }
    private String getEn() {
        return en;
    }

    private String getZh() {
        return zh;
    }

    public String getStr() {
        if (LANG.equals("zh")) return getZh();
        return getEn();
    }
}
