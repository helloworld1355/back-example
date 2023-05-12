package com.example.back_example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
//对应数据库中的表
@TableName("user")
public class User {
    //自动选择主键
    @TableId(type = IdType.AUTO)
    private int id;

    private String userName;
    private String userPwd;
    private String realName;
    private String tel;
    private String email;
    private int isDel;

    @TableField(exist = false)
    private String dep;
}
