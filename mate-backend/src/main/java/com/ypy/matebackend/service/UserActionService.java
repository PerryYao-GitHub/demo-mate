package com.ypy.matebackend.service;

import com.ypy.matebackend.common.Resp;
import com.ypy.matebackend.dto.TeamDTO;
import com.ypy.matebackend.entity.Team;
import com.ypy.matebackend.entity.User;

import java.util.List;

public interface UserActionService {
    /**
     * 查找 "常用" 用户 => 登陆时间距离当下 <= 3 天 并且在 precache 中, 每小时更新
     */
    List<User> getActiveUsers();

    Resp searchUsersByTags(List<String> tags);

    Resp getUsersRecommend(Integer userId, Integer page);

    Resp leaderCreateTeam(Integer leaderId, Team team);

    Resp leaderDeleteTeam(Integer leaderId, Integer teamId);

    Resp leaderUpdateTeam(Integer leaderId, TeamDTO teamDTO);

    Resp checkTeams(String keyword, Integer page);

    Resp checkMyCreatedTeams(Integer userId);

    Resp checkMyJoinedTeams(Integer userId);

    Resp checkOneTeam(Integer teamId);

    Resp joinTeam(Integer teamId, Integer userId);

    Resp quitTeam(Integer teamId, Integer userId);
}
