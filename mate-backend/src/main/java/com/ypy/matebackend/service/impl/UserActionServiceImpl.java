package com.ypy.matebackend.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ypy.matebackend.common.Code;
import com.ypy.matebackend.common.Resp;
import com.ypy.matebackend.common.exception.BusinessException;
import com.ypy.matebackend.dto.UserDTO;
import com.ypy.matebackend.entity.User;
import com.ypy.matebackend.mapper.UserMapper;
import com.ypy.matebackend.service.UserAccountService;
import com.ypy.matebackend.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserActionServiceImpl implements UserActionService {
    @Autowired
    private UserMapper userMapper;

    @Resource
    private UserAccountService userAccountService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Resp searchUsersByTags(List<String> tags) {
        if (CollectionUtil.isEmpty(tags)) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        // 内存查询
        /// 查询所有用户
        List<User> allUsers = userMapper.selectList(null);
        Gson gson = new Gson();
        /// 在内存中查询符合要求的标签
        List<UserDTO> dtos = allUsers.stream()
                .filter(user -> {
                    String userTagsStr = user.getTags();
                    if (StrUtil.isEmpty(userTagsStr)) return false;  // 没有加tag的用户直接 pass 掉
                    Set<String> userTagsSet = gson.fromJson(userTagsStr, new TypeToken<Set<String>>() {
                    }.getType());
                    for (String tag : tags) {
                        if (!userTagsSet.contains(tag)) return false;  // 把不含目标tag的user过滤掉
                    }
                    return true;
                }).map(UserDTO::toDTO).collect(Collectors.toList());
        return Resp.success(dtos);
    }

    @Deprecated
    private Resp searchUsersByTagsBySQL(List<String> tags) {
        if (CollectionUtil.isEmpty(tags)) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        // SQL查询
        QueryWrapper<User> qw = new QueryWrapper<>();
        for (String tag : tags) {
            qw.like("tags", tag);
        }
        List<User> users = userMapper.selectList(qw);
        List<UserDTO> dtos = users.stream().map(UserDTO::toDTO).collect(Collectors.toList());
        return Resp.success(dtos);

    }

    @Override
    public Resp getUsersRecommend(HttpServletRequest request, Integer page) {
        Integer userId = userAccountService.getUserIdFromRequest(request);
        if (page == null || page < 1) page = 1;

        String redisKey = "mate-user-recommend" + "-" + page;
        QueryWrapper<User> qw = new QueryWrapper<>();

        if (userId != null) {
            redisKey += "-" + userId;
            //  qw  // 增加用户个性化推荐
        }
        List<User> users = (List<User>) redisTemplate.opsForValue().get(redisKey);
        if (CollectionUtil.isEmpty(users)) {
            // 查数据, 存redis
            IPage<User> usersPage = userMapper.selectPage(new Page<>(page, 10), qw);
            users = usersPage.getRecords();
            redisTemplate.opsForValue().set(redisKey, users, 5, TimeUnit.MINUTES);  // 五分钟过期 !!!!! redis内存不能无限增加
            return Resp.success(users);
        } else {  // 有缓存, 直接读取
            return Resp.success(users);
        }
    }
}
