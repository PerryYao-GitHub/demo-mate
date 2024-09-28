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
import com.ypy.matebackend.common.TeamConst;
import com.ypy.matebackend.common.exception.BusinessException;
import com.ypy.matebackend.dto.TeamDTO;
import com.ypy.matebackend.dto.UserDTO;
import com.ypy.matebackend.entity.Team;
import com.ypy.matebackend.entity.User;
import com.ypy.matebackend.entity.Join;
import com.ypy.matebackend.mapper.TeamMapper;
import com.ypy.matebackend.service.UserAccountService;
import com.ypy.matebackend.service.UserActionService;
import com.ypy.matebackend.service.base_service.TeamService;
import com.ypy.matebackend.service.base_service.UserService;
import com.ypy.matebackend.service.base_service.JoinService;
import com.ypy.matebackend.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserActionServiceImpl implements UserActionService {
    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @Autowired
    private TeamMapper teamMapper;

    @Resource
    private JoinService joinService;

    @Resource
    private UserAccountService userAccountService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<User> getActiveUsers() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysAgo = now.minusDays(3);
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.ge("login_time", threeDaysAgo);
        return userService.list(qw);
    }

    @Override
    public Resp searchUsersByTags(List<String> tags) {
        if (CollectionUtil.isEmpty(tags)) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        // 内存查询
        /// 查询所有用户
        List<User> allUsers = userService.list(null);
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
        List<User> users = userService.list(qw);
        List<UserDTO> dtos = users.stream().map(UserDTO::toDTO).collect(Collectors.toList());
        return Resp.success(dtos);

    }




    /******************************************************************************************************************/
    /******************************************************************************************************************/
    /******************************************************************************************************************/
    @Override
    public Resp getUsersRecommend(Integer userId, Integer page) {
        if (page == null || page < 1) page = 1;

        List<User> activeUsers;
        String activeUsersRedisKey = "mate-precachejob-activeusers";

        // 先看 redis 中有没有 "mate-precachejob-activeusers" key
        if (Boolean.TRUE.equals(redisTemplate.hasKey(activeUsersRedisKey))) {
            activeUsers = (List<User>) redisTemplate.opsForValue().get(activeUsersRedisKey);
        } else {
            // 如果 Redis 中没有活跃用户，手动加载
            activeUsers = getActiveUsers();
            if (CollectionUtil.isNotEmpty(activeUsers)) {
                redisTemplate.opsForValue().set(activeUsersRedisKey, activeUsers, 30, TimeUnit.MINUTES);  // 临时加载，过期时间较短
            }
        }

        List<User> recommendedUsers = null;
        String redisKey = "mate-user-recommend-" + page;

        // 如果用户已登录，生成个性化推荐
        if (userId != null) {
            redisKey += "-" + userId;

            // 检查个性化推荐缓存是否存在
            if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
                recommendedUsers = (List<User>) redisTemplate.opsForValue().get(redisKey);
            } else {
                // 自定义推荐算法 (伪代码，需要你实现算法逻辑)
                recommendedUsers = getMatchUsers(userId, activeUsers);

                // 进行分页
                recommendedUsers = PaginationUtils.paginate(recommendedUsers, page, 10);

                // 只有当推荐结果不为空时才缓存
                if (CollectionUtil.isNotEmpty(recommendedUsers)) {
                    redisTemplate.opsForValue().set(redisKey, recommendedUsers, 5, TimeUnit.MINUTES);  // 五分钟过期
                }
            }
        } else {
            // 未登录用户，使用公共推荐缓存
            if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
                recommendedUsers = (List<User>) redisTemplate.opsForValue().get(redisKey);
            } else {
                // 从活跃用户中进行分页
                recommendedUsers = PaginationUtils.paginate(activeUsers, page, 10);

                // 只有当分页结果不为空时才缓存
                if (CollectionUtil.isNotEmpty(recommendedUsers)) {
                    redisTemplate.opsForValue().set(redisKey, recommendedUsers, 5, TimeUnit.MINUTES);  // 五分钟过期
                }
            }
        }

        // 如果推荐结果为空，返回空列表
        return Resp.success(recommendedUsers == null ? Collections.emptyList() : recommendedUsers);
    }

    private List<User> getMatchUsers(Integer userId, List<User> users) {
        return null;
    }
    /******************************************************************************************************************/
    /******************************************************************************************************************/
    /******************************************************************************************************************/






    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp leaderCreateTeam(Integer leaderId, Team team) {
        if (leaderId == null || leaderId <= 0) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        if (team == null) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        if (team.getId() != null) throw new BusinessException(Code.ERROR_AUTH);
        if (StrUtil.isBlank(team.getName())) throw new BusinessException(Code.ERROR_PARAMS_INVALID);
        QueryWrapper<Join> qw = new QueryWrapper<>();
        qw.eq("user_id", leaderId);
        if (joinService.count(qw) >= TeamConst.MAX_CREATE_CNT) throw new BusinessException(Code.ERROR_AUTH, "you can only create 5 teams at most");

        User leader = userService.getById(leaderId);
        Integer createCnt = leader.getCreateTeamCnt();
        if (createCnt >= TeamConst.MAX_CREATE_CNT) throw new BusinessException(Code.ERROR_AUTH, "you can only create 5 teams at most");
        leader.setCreateTeamCnt(createCnt + 1);
        Integer joinCnt = leader.getJoinTeamCnt();
        if (joinCnt >= TeamConst.MAX_JOIN_CNT) throw new BusinessException(Code.ERROR_AUTH, "you can only join 5 teams at most");
        leader.setJoinTeamCnt(joinCnt + 1);
        if (!userService.updateById(leader)) throw new BusinessException(Code.ERROR_BACKEND);

        team.setMemberCnt(1);
        team.setLeaderId(leaderId);
        team.setCreateTime(LocalDateTime.now());
        team.setUpdateTime(LocalDateTime.now());
        if (!teamService.save(team)) throw new BusinessException(Code.ERROR_BACKEND);

        Join join = new Join();
        join.setTeamId(team.getId());
        join.setUserId(leaderId);
        join.setCreateTime(LocalDateTime.now());
        if (!joinService.save(join)) throw new BusinessException(Code.ERROR_BACKEND);
        TeamDTO teamDTO = TeamDTO.toDTO(team);
        List<User> members = new ArrayList<>();
        members.add(userService.getById(leaderId));
        teamDTO.setMembers(members);
        return Resp.success(team);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp leaderDeleteTeam(Integer leaderId, Integer teamId) {
        if (teamId == null || leaderId == null) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        Team team = teamService.getById(teamId);
        if (team == null || !Objects.equals(team.getLeaderId(), leaderId)) throw new BusinessException(Code.ERROR_AUTH);
        if (!teamService.removeById(team)) throw new BusinessException(Code.ERROR_BACKEND);

        User leader = userService.getById(leaderId);
        leader.setCreateTeamCnt(Math.max(leader.getCreateTeamCnt() - 1, 0));
        if (!userService.updateById(leader)) throw new BusinessException(Code.ERROR_BACKEND);

        QueryWrapper<Join> qw = new QueryWrapper<>();
        qw.eq("team_id", teamId);
        List<Join> joins = joinService.list(qw);
        if (!joinService.removeBatchByIds(joins)) throw new BusinessException(Code.ERROR_BACKEND);
        List<Integer> memberIds = joins.stream().map(Join::getUserId).collect(Collectors.toList());
        for (Integer memberId : memberIds) {
            User member = userService.getById(memberId);
            member.setJoinTeamCnt(Math.max(member.getJoinTeamCnt() - 1, 0));
            if (!userService.updateById(member)) throw new BusinessException(Code.ERROR_BACKEND);
        }
        return Resp.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp checkTeams(String keyword, Integer page) {
        if (page == null || page <= 0) page = 1;
        QueryWrapper<Team> tQW = new QueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            tQW.and(wrapper -> wrapper
                    .like("name", keyword)
                    .or()
                    .like("description", keyword)
            );
        }
        tQW.orderByDesc("create_time");
        IPage<Team> teamIPage = teamService.page(new Page<>(page, 10), tQW);
        return Resp.success(teamIPage.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp checkMyCreatedTeams(Integer userId) {
        QueryWrapper<Team> qw = new QueryWrapper<>();
        qw.eq("leader_id", userId);
        List<Team> teams = teamService.list(qw);
        return Resp.success(teams);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp checkMyJoinedTeams(Integer userId) {
        QueryWrapper<Join> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        List<Join> joins = joinService.list(qw);
        List<Integer> teamIds = joins.stream().map(Join::getTeamId).collect(Collectors.toList());
        List<Team> teams = teamService.listByIds(teamIds);
        return Resp.success(teams);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp checkOneTeam(Integer teamId) {
        TeamDTO teamDTO = TeamDTO.toDTO(teamService.getById(teamId));
        teamDTO.setMembers(teamMapper.getMembersByTeamId(teamId));
        return Resp.success(teamDTO);
    }

    @Override
    public Resp leaderUpdateTeam(Integer leaderId, TeamDTO teamDTO) {
        if (teamDTO == null || leaderId == null || teamDTO.getId() == null) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        if (!Objects.equals(teamDTO.getLeaderId(), leaderId)) throw new BusinessException(Code.ERROR_AUTH);
        Team team = teamService.getById(teamDTO.getId());
        if (StrUtil.isNotBlank(teamDTO.getName())) team.setName(teamDTO.getName());
        if (StrUtil.isNotBlank(teamDTO.getDescription())) team.setDescription(teamDTO.getDescription());
        team.setUpdateTime(LocalDateTime.now());
        if (!teamService.updateById(team)) throw new BusinessException(Code.ERROR_BACKEND);
        return Resp.success();
    }

    @Override
    public Resp joinTeam(Integer teamId, Integer userId) {
        QueryWrapper<Join> qw = new QueryWrapper<>();
        qw.eq("team_id", teamId);
        qw.eq("user_id", userId);
        if (joinService.count(qw) > 0) throw new BusinessException(Code.ERROR_AUTH);
        Team joinedTeam = teamService.getById(teamId);
        if (joinedTeam == null || joinedTeam.getMemberCnt() >= TeamConst.MAX_MEMBER_CNT) throw new BusinessException(Code.ERROR_AUTH, "team is full");
        User user = userService.getById(userId);
        if (user.getJoinTeamCnt() >= TeamConst.MAX_JOIN_CNT) throw new BusinessException(Code.ERROR_AUTH, "you can only join 5 teams at most");

        Join join = new Join();
        join.setTeamId(teamId);
        join.setUserId(userId);
        join.setCreateTime(LocalDateTime.now());
        if (!joinService.save(join)) throw new BusinessException(Code.ERROR_BACKEND);
        joinedTeam.setMemberCnt(joinedTeam.getMemberCnt() + 1);
        user.setJoinTeamCnt(user.getJoinTeamCnt() + 1);
        if (!teamService.updateById(joinedTeam)) throw new BusinessException(Code.ERROR_BACKEND);
        if (!userService.updateById(user)) throw new BusinessException(Code.ERROR_BACKEND);
        return Resp.success();
    }

    @Override
    public Resp quitTeam(Integer teamId, Integer userId) {
        Team team = teamService.getById(teamId);
        if (team == null) throw new BusinessException(Code.ERROR_PARAMS_INVALID);
        User user = userService.getById(userId);
        if (user == null) throw new BusinessException(Code.ERROR_PARAMS_INVALID);
        if (Objects.equals(team.getLeaderId(), userId)) throw new BusinessException(Code.ERROR_AUTH, "leader can't quit a team");
        QueryWrapper<Join> qw = new QueryWrapper<>();
        qw.eq("team_id", teamId);
        qw.eq("user_id", userId);
        if (joinService.count(qw) == 0) throw new BusinessException(Code.ERROR_AUTH);
        if (!joinService.remove(qw)) throw new BusinessException(Code.ERROR_BACKEND);
        team.setMemberCnt(Math.max(team.getMemberCnt() - 1, 0));
        user.setJoinTeamCnt(Math.max(user.getJoinTeamCnt() - 1, 0));
        if (!teamService.updateById(team)) throw new BusinessException(Code.ERROR_BACKEND);
        if (!userService.updateById(user)) throw new BusinessException(Code.ERROR_BACKEND);
        return Resp.success();
    }
}
