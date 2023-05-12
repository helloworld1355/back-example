package com.example.back_example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.back_example.entity.User;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
