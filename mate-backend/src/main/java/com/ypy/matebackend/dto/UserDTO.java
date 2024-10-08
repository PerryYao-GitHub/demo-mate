package com.ypy.matebackend.dto;

import com.ypy.matebackend.entity.Team;
import com.ypy.matebackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends User{
    private List<Team> joiningTeams;  // 增加字段

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setAccount(user.getAccount());
        dto.setName(user.getName());
        dto.setAvatar(user.getAvatar());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setTags(user.getTags());
        dto.setCreateTeamCnt(user.getCreateTeamCnt());
        dto.setJoinTeamCnt(user.getJoinTeamCnt());
        dto.setStatus(user.getStatus());
        dto.setLoginTime(user.getLoginTime());
        dto.setCreateTime(user.getCreateTime());
        return dto;
    }
}
