package com.ypy.matebackend.dto;

import com.ypy.matebackend.entity.Team;
import com.ypy.matebackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO extends Team {
    private List<User> members;

    public static TeamDTO toDTO(Team team) {
        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setDescription(team.getDescription());
        dto.setMemberCnt(team.getMemberCnt());
        dto.setLeaderId(team.getLeaderId());
        dto.setCreateTime(team.getCreateTime());
        dto.setUpdateTime(team.getUpdateTime());
        return dto;
    }
}
