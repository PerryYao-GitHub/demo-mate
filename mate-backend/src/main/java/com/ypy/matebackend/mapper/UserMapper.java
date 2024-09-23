package com.ypy.matebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ypy.matebackend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT status FROM t_user WHERE id = #{userId}")
    Byte checkUserNotBanned(Integer userId);
}
