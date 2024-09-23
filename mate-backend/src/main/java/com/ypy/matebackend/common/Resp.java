package com.ypy.matebackend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resp {
    private Integer code;
    private String msg;
    private String description;
    private Object data;

    // 全部参数
    public static Resp success(String description, Object data) {
        Resp resp = new Resp();
        resp.setCode(Code.SUCCESS.getCode());
        resp.setMsg(Code.SUCCESS.getMsg());
        resp.setDescription(description);
        resp.setData(data);
        return resp;
    }

    // 只有数据，没有des
    public static Resp success(Object data) {
        return success("", data);
    }

    // 没有消息也没有数据
    public static Resp success() {
        return success("", null);
    }

    public static Resp error(Code code, String description) {
        Resp resp = new Resp();
        resp.setCode(code.getCode());
        resp.setMsg(code.getMsg());
        resp.setDescription(description);
        resp.setData(null);
        return resp;
    }

    public static Resp error(Code code) {
        Resp resp = Resp.error(Code.ERROR_PARAMS_INVALID);
        resp.setDescription("");
        return resp;
    }
}
