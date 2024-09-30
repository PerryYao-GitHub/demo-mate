package com.ypy.matebackend.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class GsonUtils {

    public static List<String> convertJsonToList(String json) {
        Gson gson = new Gson();
        List<String> list = gson.fromJson(json, new TypeToken<List<String>>() {}.getType());
        return list;
    }

    // List<String> 转换为 JSON 字符串
    public static String convertListToJson(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
