package com.ypy.matebackend.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String account;

    @JsonIgnore
    private String password;

    private String name;

    private String avatar;

    private String phone;

    private String email;

    private String tags;  // JSON 列表

    private Integer createTeamCnt;

    private Integer joinTeamCnt;

    private Byte status;

    private Byte role;

    private LocalDateTime loginTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Byte deleted;

    private static final long serialVersionUID = 1L;
}
