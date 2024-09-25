package com.ypy.matebackend.service;

import com.ypy.matebackend.common.Resp;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserActionService {
    Resp searchUsersByTags(List<String> tags);

    Resp getUsersRecommend(HttpServletRequest request, Integer page);
}
