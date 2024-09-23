package com.ypy.matebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_tag")
public class Tag {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String makerId;

    private Byte isParent;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Byte deleted;
}

