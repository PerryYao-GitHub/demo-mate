package com.ypy.matebackend.service;

import com.ypy.matebackend.common.Resp;

import java.util.List;

public interface UserActionService {
    Resp searchUsersByTags(List<String> tags);
}
