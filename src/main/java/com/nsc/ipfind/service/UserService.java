package com.nsc.ipfind.service;

import com.nsc.ipfind.pojos.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Service
* @createDate 2025-09-18 15:43:55
*/
public interface UserService extends IService<User> {

    User getUserByUsername(String username);
}
