package com.ypy.matebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName r_user_team
 */
@TableName(value ="r_join")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Join implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer teamId;

    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;

    private static final long serialVersionUID = 1L;
}
