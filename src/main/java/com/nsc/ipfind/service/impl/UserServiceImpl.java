package com.nsc.ipfind.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsc.ipfind.pojos.User;
import com.nsc.ipfind.service.UserService;
import com.nsc.ipfind.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-09-19 13:54:50
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{


    @Override
    public User getUserByUsername(String username) {
        LambdaQueryChainWrapper<User> queryWrapper = new LambdaQueryChainWrapper<>(getBaseMapper());
        return queryWrapper.eq(User::getUsername, username).one();
    }
}




