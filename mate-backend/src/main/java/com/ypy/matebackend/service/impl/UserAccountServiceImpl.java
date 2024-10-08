package com.ypy.matebackend.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ypy.matebackend.common.*;
import com.ypy.matebackend.common.exception.BusinessException;
import com.ypy.matebackend.dto.AccountPasswordDTO;
import com.ypy.matebackend.dto.UserDTO;
import com.ypy.matebackend.entity.User;
import com.ypy.matebackend.mapper.UserMapper;
import com.ypy.matebackend.service.UserAccountService;

import com.ypy.matebackend.utils.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
* @author 14870
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2024-09-17 22:11:05
*/
@Service
public class UserAccountServiceImpl implements UserAccountService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer getUserIdFromRequest(HttpServletRequest request) {
        // 从Session中获取用户信息
        Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
        if (user == null) return null;
        return (Integer) user.get("id");
    }

    @Override
    public Resp register(AccountPasswordDTO dto) {
        String account = dto.getAccount();
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();

        // 判断三个字符串均不为空
        if (!StrUtil.isAllNotEmpty(account, password, confirmPassword)) throw new BusinessException(Code.ERROR_PARAMS_NULL, Description.EMPTY_ERROR.getStr());

        // 判断 confirmPassword 是否与 password 相等
        if (!password.equals(confirmPassword)) throw new BusinessException(Code.ERROR_PARAMS_INVALID, Description.WRONG_CONFIRM_PASSWORD.getStr());

        // 判断 account 中是否含有 a-z, A-Z, 0-9 以及下划线以外的字符
        // 使用正则表达式：^[a-zA-Z0-9_]+$
        String accountRegex = "^[a-zA-Z0-9_]+$";
        if (!Pattern.matches(accountRegex, account)) throw new BusinessException(Code.ERROR_PARAMS_INVALID, Description.WRONG_ACCOUNT_FORMAT.getStr());

        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("account", account);
        if (userMapper.exists(qw)) throw new BusinessException(Code.ERROR_AUTH, Description.ACCOUNT_EXISTS.getStr());

        User user = new User();
        user.setAccount(account);
        user.setName("new user " + account);
        user.setPassword(SecureUtil.md5(password));
        user.setCreateTeamCnt(0);
        user.setJoinTeamCnt(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(UserStatus.AVAILABLE.getCode());
        user.setRole(UserRole.USER.getCode());
        userMapper.insert(user);
        return Resp.success(Description.REGISTER_SUCCESS.getStr(), null);
    }

    @Override
    public Resp login(AccountPasswordDTO dto, HttpServletRequest request) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("account", dto.getAccount());
        qw.eq("password", SecureUtil.md5(dto.getPassword()));
        User user = userMapper.selectOne(qw);
        if (user == null) throw new BusinessException(Code.ERROR_PARAMS_INVALID, Description.WRONG_ACCOUNT_PASSWORD.getStr());
        user.setLoginTime(LocalDateTime.now());
        userMapper.updateById(user);
        // 将用户信息存入 session
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("account", user.getAccount());
        userInfo.put("role", user.getRole());
        request.getSession().setAttribute("user", userInfo);
        return Resp.success(Description.LOGIN_SUCCESS.getStr(), UserDTO.toDTO(user));
    }

    @Override
    public Resp logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return Resp.success("logout success", null);
    }

    @Override
    public Resp changePassword(Integer id, AccountPasswordDTO dto) {
        String account = dto.getAccount();
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();
        String newPassword = dto.getNewPassword();

        // 判空
        if (!StrUtil.isAllNotEmpty(account, password, confirmPassword, newPassword)) throw new BusinessException(Code.ERROR_PARAMS_NULL, Description.EMPTY_ERROR.getStr());

        // 判断 confirmPassword 是否与 newPassword 相等
        if (!newPassword.equals(confirmPassword)) throw new BusinessException(Code.ERROR_PARAMS_INVALID, Description.WRONG_CONFIRM_PASSWORD.getStr());

        // 判断是不是当前用户
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("account", account);
        qw.eq("password", SecureUtil.md5(password));
        User user = userMapper.selectOne(qw);
        if (user == null) throw new BusinessException(Code.ERROR_AUTH, Description.WRONG_ACCOUNT_PASSWORD.getStr());
        user.setPassword(SecureUtil.md5(newPassword));
        userMapper.updateById(user);
        return Resp.success(Description.CHANGE_PASSWORD_SUCCESS.getStr(), null);
    }

    @Override
    public Resp delAccount(Integer id, AccountPasswordDTO dto) {
        String account = dto.getAccount();
        String password = dto.getPassword();
        if (!StrUtil.isAllNotEmpty(account, password)) throw new BusinessException(Code.ERROR_PARAMS_NULL, Description.EMPTY_ERROR.getStr());
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("id", id);
        qw.eq("account", account);
        qw.eq("password", SecureUtil.md5(password));
        User user = userMapper.selectOne(qw);
        if (user == null) throw new BusinessException(Code.ERROR_AUTH, Description.WRONG_ACCOUNT_PASSWORD.getStr());
        userMapper.deleteById(user);
        return Resp.success(Description.DEL_ACCOUNT_SUCCESS.getStr(), null);
    }

    @Override
    public Resp checkAccount(Integer id) {
        User user = userMapper.selectById(id);
        UserDTO dto = UserDTO.toDTO(user);
        return Resp.success(dto);
    }

    @Override
    public Resp editAccount(Integer id, UserDTO dto) {
        User user = userMapper.selectById(id);
        if ( !StrUtil.isBlank( dto.getName() )) user.setName(dto.getName());
        if ( !StrUtil.isBlank( dto.getEmail() ))user.setEmail(dto.getEmail());
        if ( !StrUtil.isBlank( dto.getPhone() ))user.setPhone(dto.getPhone());
        if ( !StrUtil.isBlank( dto.getAvatar() ))user.setAvatar(dto.getAvatar());
        if ( StrUtil.isNotBlank( dto.getTags() ) ) {
            String tagsStr = dto.getTags();
            List<String> tags = GsonUtils.convertJsonToList(tagsStr);
            if (tags.size() > 5) throw new BusinessException(Code.ERROR_PARAMS_INVALID, "a user can only have 5 tags at most");
            for (String tag : tags) {
                if (! TagConst.isValidTag(tag)) throw new BusinessException(Code.ERROR_PARAMS_INVALID, "wrong tags");
            }
            Collections.sort(tags);  // 把 tag 排序后存入
            tagsStr = GsonUtils.convertListToJson(tags);
            user.setTags(tagsStr);
        }
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return Resp.success(Description.EDIT_ACCOUNT_SUCCESS.getStr(), null);
    }
}
