package com.ypy.matebackend.controller;

import com.ypy.matebackend.common.Code;
import com.ypy.matebackend.common.Resp;
import com.ypy.matebackend.common.UserRole;
import com.ypy.matebackend.common.exception.BusinessException;
import com.ypy.matebackend.dto.AccountPasswordDTO;
import com.ypy.matebackend.dto.UserDTO;
import com.ypy.matebackend.service.UserAccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserAccountController {
    @Resource
    private UserAccountService userAccountService;

    static Integer getUserIdFromRequest(HttpServletRequest request) {
        // 从Session中获取用户信息
        Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
        if (user == null) throw new BusinessException(Code.ERROR_AUTH);
        return (Integer) user.get("id");
    }

    // 以下操作不需要get id
    @PostMapping("/register")
    public Resp register(@RequestBody AccountPasswordDTO dto) {
        return userAccountService.register(dto);
    }

    @PostMapping("/login")
    public Resp login(@RequestBody AccountPasswordDTO dto, HttpServletRequest request) {
        return userAccountService.login(dto, request);
    }

    @PostMapping("/logout")
    public Resp logout(HttpServletRequest request) {
        return userAccountService.logout(request);
    }

    // 后面需要 get id
    @PostMapping("/change_password")
    public Resp changePassword(@RequestBody AccountPasswordDTO dto, HttpServletRequest request) {
        Integer id = getUserIdFromRequest(request);
        return userAccountService.changePassword(id, dto);
    }

    @PostMapping("/del_account")
    public Resp delAccount(@RequestBody AccountPasswordDTO dto, HttpServletRequest request) {
        Integer id = getUserIdFromRequest(request);
        return userAccountService.delAccount(id, dto);
    }

    @GetMapping("/check_account")
    public Resp checkAccount(HttpServletRequest request) {
        Integer id = getUserIdFromRequest(request);
        return userAccountService.checkAccount(id);
    }

    @PostMapping("/edit_account")
    public Resp editAccount(@RequestBody UserDTO dto, HttpServletRequest request) {
        Integer id = getUserIdFromRequest(request);
        return userAccountService.editAccount(id, dto);
    }
}
