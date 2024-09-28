package com.ypy.matebackend.mapper;

import com.ypy.matebackend.dto.TeamDTO;
import com.ypy.matebackend.entity.Team;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ypy.matebackend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 14870
* @description 针对表【t_team】的数据库操作Mapper
* @createDate 2024-09-26 19:25:37
* @Entity com.ypy.matebackend.entity.Team
*/
@Mapper
public interface TeamMapper extends BaseMapper<Team> {
    @Select("SELECT u.* " +
            "FROM r_join j " +
            "JOIN t_user u ON j.user_id = u.id " +
            "WHERE j.team_id = #{teamId} AND j.deleted = 0")  // 自己写的sql要把加删除的逻辑补上
    List<User> getMembersByTeamId(@Param("teamId") Integer teamId);
}




