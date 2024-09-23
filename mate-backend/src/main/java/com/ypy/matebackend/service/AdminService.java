package com.ypy.matebackend.service;

import com.ypy.matebackend.common.Resp;

public interface AdminService {
    Resp switchUserStatus(Integer userId);
}
