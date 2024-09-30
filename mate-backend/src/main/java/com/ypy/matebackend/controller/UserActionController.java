package com.ypy.matebackend.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.ypy.matebackend.common.Code;
import com.ypy.matebackend.common.Resp;
import com.ypy.matebackend.common.exception.BusinessException;
import com.ypy.matebackend.dto.TeamDTO;
import com.ypy.matebackend.entity.Team;
import com.ypy.matebackend.service.UserAccountService;
import com.ypy.matebackend.service.UserActionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserActionController {
    @Resource
    private UserActionService userActionService;
    @Resource
    private UserAccountService userAccountService;

    @GetMapping("/get/all/tags")
    public Resp getAllTags() {
        return userActionService.getAllTags();
    }

    @GetMapping("/search_users_by_tags")
    public Resp searchUsersByTags(@RequestParam List<String> tagNames) {
        if (CollectionUtil.isEmpty(tagNames)) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        return userActionService.searchUsersByTags(tagNames);
    }

    @GetMapping("/check/one/user")
    public Resp checkOneUser(HttpServletRequest request, @RequestParam Integer userId) {
        Integer visitorId = userAccountService.getUserIdFromRequest(request);
        if (visitorId == null) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        return userActionService.checkOneUser(visitorId, userId);
    }

    @GetMapping("/users/recommend")
    public Resp getUsersRecommend(HttpServletRequest request, @RequestParam Integer page) {
        Integer userId = userAccountService.getUserIdFromRequest(request);
        return userActionService.getUsersRecommend(userId, page);
    }

    @PostMapping("/create/team")
    public Resp createTeam(@RequestBody Team team, HttpServletRequest request) {
        Integer leaderId = userAccountService.getUserIdFromRequest(request);
        return userActionService.leaderCreateTeam(leaderId, team);
    }

    @GetMapping("/delete/team")
    public Resp deleteTeam(@RequestParam Integer teamId, HttpServletRequest request) {
        Integer leaderId = userAccountService.getUserIdFromRequest(request);
        return userActionService.leaderDeleteTeam(leaderId, teamId);
    }

    @PostMapping("/update/team")
    public Resp updateTeam(@RequestBody TeamDTO teamDTO, HttpServletRequest request) {
        Integer leaderId = userAccountService.getUserIdFromRequest(request);
        return userActionService.leaderUpdateTeam(leaderId, teamDTO);
    }

    @GetMapping("/check/teams")
    public Resp checkTeams(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer page
    ) {
        return userActionService.checkTeams(keyword, page);
    }

    @GetMapping("/check/teams/created")
    public Resp checkTeamsCreated(HttpServletRequest request) {
        Integer userId = userAccountService.getUserIdFromRequest(request);
        return userActionService.checkMyCreatedTeams(userId);
    }

    @GetMapping("/check/teams/joined")
    public Resp checkTeamsJoined(HttpServletRequest request) {
        Integer userId = userAccountService.getUserIdFromRequest(request);
        return userActionService.checkMyJoinedTeams(userId);
    }

    @GetMapping("/check/one/team")
    public Resp checkOneTeam(@RequestParam Integer teamId) {
        return userActionService.checkOneTeam(teamId);
    }

    @GetMapping("/join/team")
    public Resp joinTeam(@RequestParam Integer teamId, HttpServletRequest request) {
        Integer userId = userAccountService.getUserIdFromRequest(request);
        return userActionService.joinTeam(teamId, userId);
    }

    @GetMapping("/quit/team")
    public Resp quitTeam(@RequestParam Integer teamId, HttpServletRequest request) {
        Integer userId = userAccountService.getUserIdFromRequest(request);
        return userActionService.quitTeam(teamId, userId);
    }
}
