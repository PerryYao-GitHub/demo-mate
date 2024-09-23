package com.ypy.matebackend.controller;

import cn.hutool.core.util.StrUtil;
import com.ypy.matebackend.common.Code;
import com.ypy.matebackend.common.Description;
import com.ypy.matebackend.common.Resp;
import com.ypy.matebackend.common.UserRole;
import com.ypy.matebackend.common.exception.BusinessException;
import com.ypy.matebackend.dto.AccountPasswordDTO;
import com.ypy.matebackend.service.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    // 管理员登录
    @PostMapping("/login")
    public Resp login(@RequestBody AccountPasswordDTO dto, HttpServletRequest request) {
        String account = dto.getAccount();
        String password = dto.getPassword();
        if (StrUtil.isBlank(account) || StrUtil.isBlank(password)) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        if (!(account.equals("admin") && password.equals("123456"))) throw new BusinessException(Code.ERROR_PARAMS_INVALID, "wrong username or password");
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", 0);
        userInfo.put("account", account);
        userInfo.put("role", UserRole.ADMIN.getCode());
        request.setAttribute("user", userInfo);
        return Resp.success(Description.LOGIN_SUCCESS.getStr());
    }

    // 禁评某用户
    @GetMapping("/user/switch_status/{id}")
    public Resp switchUserStatus(@PathVariable Integer id) {
        if (id == null || id <= 0) return Resp.error(Code.ERROR_PARAMS_INVALID);
        return adminService.switchUserStatus(id);
    }
}
