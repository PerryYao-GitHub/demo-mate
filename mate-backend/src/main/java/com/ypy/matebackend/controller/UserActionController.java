package com.ypy.matebackend.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.ypy.matebackend.common.Code;
import com.ypy.matebackend.common.Resp;
import com.ypy.matebackend.common.exception.BusinessException;
import com.ypy.matebackend.service.UserActionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserActionController {
    @Resource
    private UserActionService userActionService;

    @GetMapping("/search_users_by_tags")
    public Resp searchUsersByTags(@RequestParam List<String> tagNames) {
        if (CollectionUtil.isEmpty(tagNames)) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        return userActionService.searchUsersByTags(tagNames);
    }
}
