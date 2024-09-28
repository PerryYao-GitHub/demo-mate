package com.ypy.matebackend.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName t_team
 */
@TableName(value ="t_team")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String description;

    private Integer memberCnt;

    private Integer leaderId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    private static final long serialVersionUID = 1L;
}
