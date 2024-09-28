package com.ypy.matebackend.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaginationUtils {

    /**
     * 静态分页工具方法，支持泛型
     * @param list 要分页的数据列表
     * @param page 当前页码（从1开始）
     * @param pageSize 每页显示的数据数量
     * @param <T> 泛型类型
     * @return 分页后的数据列表
     */
    public static <T> List<T> paginate(List<T> list, int page, int pageSize) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();  // 如果列表为空，返回空列表
        }

        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, list.size());

        if (fromIndex > list.size()) {
            return Collections.emptyList(); // 如果页码超出范围，返回空列表
        }

        return new ArrayList<>(list.subList(fromIndex, toIndex));  // 将 SubList 转换为常规的 ArrayList 以确保 Redis 可以序列化
    }
}

