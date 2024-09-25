package com.ypy.matebackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ypy.matebackend.common.Resp;
import com.ypy.matebackend.dto.AccountPasswordDTO;
import com.ypy.matebackend.dto.UserDTO;
import com.ypy.matebackend.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author 14870
* @description 针对表【t_user】的数据库操作Service
* @createDate 2024-09-17 22:11:05
*/
public interface UserAccountService {
    Integer getUserIdFromRequest(HttpServletRequest request);



    Resp register(AccountPasswordDTO dto);
    Resp login(AccountPasswordDTO dto, HttpServletRequest request);
    Resp logout(HttpServletRequest request);
    Resp changePassword(Integer id, AccountPasswordDTO dto);

    Resp delAccount(Integer id, AccountPasswordDTO dto);
    Resp checkAccount(Integer id);

    Resp editAccount(Integer id, UserDTO dto);
}
