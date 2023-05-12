package com.example.back_example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.back_example.common.CommonResult;
import com.example.back_example.entity.User;
import com.example.back_example.mapper.UserMapper;
import com.example.back_example.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public CommonResult<User> login(String userName, String password) {
        CommonResult<User> rs = new CommonResult<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName)
                .eq("user_pwd",password);
        try {
            List<User> list = userMapper.selectList(queryWrapper);
            if (list.size() > 0) {
                if (list.get(0).getIsDel() == 1) {
                    rs.setCode(-1);
                    rs.setMsg("该账号已被禁用");

                } else {
                    rs.setCode(0);
                    rs.setMsg("登录成功");
                    rs.setData(list.get(0));
                    return rs;
                }
            } else {
                rs.setCode(-1);
                rs.setMsg("用户名或密码错误");

            }
            return rs;
        }catch (Exception exception){
            rs.setCode(-1);
            rs.setMsg("出现错误："+exception.getMessage());
            return rs;
        }
    }

    @Override
    public CommonResult<List<User>> search(String userName, String realName, Integer page, Integer size) {
        CommonResult<List<User>> rs = new CommonResult<>();
        try {
            Page<User> pg = new Page<>(page, size);
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            if(userName!=null&&!userName.isEmpty())
                queryWrapper.eq("user_name",userName);
            if(realName!=null&&!realName.isEmpty())
                queryWrapper.eq("real_name",realName);
            userMapper.selectPage(pg, queryWrapper);
            List<User> list = pg.getRecords();
            long count = pg.getTotal();

            rs.setCode(0);
            rs.setCount(count);
            rs.setData(list);
            rs.setMsg("查询成功");
            return rs;
        }catch (Exception exception){
            rs.setCode(-1);
            rs.setMsg("发生错误："+exception.getMessage());
            return rs;
        }
    }

    @Override
    public CommonResult<Boolean> save(User user) {
        CommonResult<Boolean> rs = new CommonResult<Boolean>();
        try {
            // user.id==0添加
            if (user.getId() == 0) {
                int k = userMapper.insert(user);
                if (k > 0) {
                    rs.setCode(0);
                    rs.setMsg("添加成功");

                } else {
                    rs.setCode(-1);
                    rs.setMsg("添加失败");
                    rs.setData(false);
                }
            } else {
                int k = userMapper.updateById(user);
                if (k > 0) {
                    rs.setCode(0);
                    rs.setMsg("修改成功");
                    rs.setData(true);
                } else {
                    rs.setCode(-1);
                    rs.setMsg("修改失败");
                    rs.setData(false);
                }
            }
            return rs;
        }catch (Exception exception){
            rs.setCode(-1);
            rs.setMsg("发生错误："+exception.getMessage());
            return rs;
        }
    }

    @Override
    public CommonResult<Boolean> delete(Integer id) {
        CommonResult<Boolean> rs = new CommonResult<>();
        try {
            int k = userMapper.deleteById(id);
            if (k > 0) {
                rs.setCode(0);
                rs.setMsg("删除成功");

            } else {
                rs.setCode(-1);
                rs.setMsg("删除失败");
            }
            return rs;
        }catch (Exception exception){
            rs.setCode(-1);
            rs.setMsg("发生错误："+exception.getMessage());
            return rs;
        }
    }

    @Override
    public CommonResult<Boolean> resetPassword(Integer id) {
        CommonResult<Boolean> rs = new CommonResult<>();
        try {
            User us = userMapper.selectById(id);
            if (us == null) {
                rs.setCode(-1);
                rs.setMsg("该用户不存在");
            } else {
                us.setUserPwd("123456");
                int k = userMapper.updateById(us);
                if (k > 0) {
                    rs.setCode(0);
                    rs.setMsg("重置密码成功");
                } else {
                    rs.setCode(-1);
                    rs.setMsg("重置密码失败");
                }
            }
            return rs;
        }catch (Exception exception){
            rs.setCode(-1);
            rs.setMsg("发生错误："+exception.getMessage());
            return rs;
        }
    }

    @Override
    public User getUserById(Integer id) {
        //userMapper.selectById(id);
        User user = userMapper.selectById(id);
        return user;
    }

    @Override
    public CommonResult changePwd(Integer userId, String oldPwd, String newPwd) {
        CommonResult rs = new CommonResult();
        User user = userMapper.selectById(userId);
        if(!user.getUserPwd().equals(oldPwd)){
            rs.setCode(-1);
            rs.setMsg("原始密码错误");
            return rs;
        }
        user.setUserPwd(newPwd);
        try{
            userMapper.updateById(user);
            rs.setCode(0);
            rs.setMsg("修改密码成功");
            return rs;
        }catch (Exception ex){
            rs.setCode(-2);
            rs.setMsg("发生错误"+ex.getMessage());
            return rs;
        }

    }
}
