package com.ypy.matebackend.dto;

import com.ypy.matebackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;

    private String account;

    private String name; // 可改

    private String avatar;  // 可改

    private String phone;  // 可改

    private String email;  // 可改

    private String tags;  // 可改

    private Byte status;

    private LocalDateTime loginTime;

    private LocalDateTime createTime;

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setAccount(user.getAccount());
        dto.setName(user.getName());
        dto.setAvatar(user.getAvatar());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setTags(user.getTags());
        dto.setStatus(user.getStatus());
        dto.setLoginTime(user.getLoginTime());
        dto.setCreateTime(user.getCreateTime());
        return dto;
    }
}
