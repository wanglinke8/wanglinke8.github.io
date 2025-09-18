package com.nsc.ipfind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsc.ipfind.pojos.User;
import com.nsc.ipfind.service.UserService;
import com.nsc.ipfind.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-09-18 15:43:55
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return this.getOne(wrapper);
    }
}




