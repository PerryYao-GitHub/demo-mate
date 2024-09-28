package com.ypy.matebackend.service.impl.base_service_impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ypy.matebackend.entity.Team;
import com.ypy.matebackend.service.base_service.TeamService;
import com.ypy.matebackend.mapper.TeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 14870
* @description 针对表【t_team】的数据库操作Service实现
* @createDate 2024-09-26 19:25:37
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

}




