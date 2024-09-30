package com.ypy.matebackend.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum TagConst {
    Java("java","tech"),
    Python("python","tech"),
    CPP("c++","tech"),
    JS("js","tech"),
    SpringBoot("spring-boot","tech"),
    Vue("vue","tech"),
    React("react","tech"),
    Anime("anime","hobby"),
    Film("film","hobby"),
    Music("music","hobby"),
    Novel("novel","hobby"),
    Sleep("sleep","hobby"),
    Football("football","sport"),
    Badminton("badminton","sport"),
    PingPong("ping-pong","sport"),
    Basketball("basketball","sport"),;

    private final String tag;
    private final String type;

    TagConst(String tag, String type) {
        this.tag = tag;
        this.type = type;
    }

    public static boolean isValidTag(String tag) {
        for (TagConst tagConst : TagConst.values()) {
            if (tagConst.getTag().equals(tag)) return true;
        }
        return false;
    }

    // 修改后的方法，将数据格式调整为前端需要的格式
    public static List<Map<String, Object>> getAllTagsForFrontend() {
        Map<String, List<Map<String, String>>> map = new HashMap<>();
        for (TagConst tagConst : TagConst.values()) {
            String type = tagConst.getType();
            if (!map.containsKey(type)) {
                map.put(type, new ArrayList<>());
            }
            // 为每个子标签创建一个带有 text 和 id 的 Map
            Map<String, String> childTag = new HashMap<>();
            childTag.put("text", tagConst.getTag());
            childTag.put("id", tagConst.getTag());
            map.get(type).add(childTag);
        }

        // 将 Map 转换为前端所需的 List<Map<String, Object>> 格式
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, String>>> entry : map.entrySet()) {
            Map<String, Object> parentTag = new HashMap<>();
            parentTag.put("text", entry.getKey());
            parentTag.put("children", entry.getValue());
            result.add(parentTag);
        }

        return result;
    }
}
