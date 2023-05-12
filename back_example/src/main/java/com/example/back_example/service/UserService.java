package com.example.back_example.service;

import com.example.back_example.common.CommonResult;
import com.example.back_example.entity.User;


import java.util.List;

public interface UserService {
    //用户登录验证
    public CommonResult<User> login(String userName, String password);
    // 查询用户分页
    public CommonResult<List<User>> search(String userName, String realName, Integer page, Integer size);
    // 保存用户
    public CommonResult<Boolean> save(User user);
    // 删除用户
    CommonResult<Boolean> delete(Integer id);
    // 重置密码
    CommonResult<Boolean> resetPassword(Integer id);

    User getUserById(Integer id);

    CommonResult changePwd(Integer userId,String oldPwd,String newPwd);


}
