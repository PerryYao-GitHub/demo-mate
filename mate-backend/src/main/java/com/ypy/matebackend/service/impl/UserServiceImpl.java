package com.ypy.matebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ypy.matebackend.entity.User;
import com.ypy.matebackend.mapper.UserMapper;
import com.ypy.matebackend.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
