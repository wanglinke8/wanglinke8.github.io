package com.nsc.ipfind.service;

import com.nsc.ipfind.pojos.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Service
* @createDate 2025-09-19 15:20:38
*/
public interface UserService extends IService<User> {

    User getUserByUsername(String zhanghao);

    User getByname(String name);

    User updateUserInfo(String token, String newName, String newZhanghao, MultipartFile avatarFile);
}
