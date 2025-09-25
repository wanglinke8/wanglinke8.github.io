package com.nsc.ipfind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsc.ipfind.pojos.User;
import com.nsc.ipfind.service.UserService;
import com.nsc.ipfind.mapper.UserMapper;
import com.nsc.ipfind.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-09-19 15:20:38
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil; // 注入您创建的JWT工具类

    // 从配置文件中读取上传路径
    @Value("${file.upload.path:uploads/}")
    private String uploadPath;

    // 从配置文件中读取访问路径前缀
    @Value("${file.access.path:/uploads/}")
    private String accessPath;
    @Override
    public User getUserByUsername(String zhanghao) {
        LambdaQueryChainWrapper<User> queryWrapper = new LambdaQueryChainWrapper<>(this.getBaseMapper());
        queryWrapper.eq(User::getZhanghao, zhanghao);
        return queryWrapper.one();
    }

    @Override
    public User getByname(String name) {
        LambdaQueryChainWrapper<User> queryWrapper = new LambdaQueryChainWrapper<>(this.getBaseMapper());
        queryWrapper.eq(User::getName, name);
        return queryWrapper.one();
    }

    @Override
    public User updateUserInfo(String token, String newName, String newZhanghao, MultipartFile avatarFile) {
        // 1. 从 Token 解析出 zhanghao (或用户ID)
        String currentZhanghao = jwtUtil.getZhanghaoFromToken(token);
        if (currentZhanghao == null || currentZhanghao.isEmpty()) {
            throw new IllegalArgumentException("无效的 Token");
        }

        // 2. 查询当前用户信息
        User currentUser = getUserByUsername(currentZhanghao);
        if (currentUser == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 3. 检查新账号是否已被其他用户使用 (如果新账号与旧账号不同)
        if (!newZhanghao.equals(currentUser.getZhanghao())) {
            QueryWrapper<User> checkQuery = new QueryWrapper<>();
            checkQuery.eq("zhanghao", newZhanghao).ne("id", currentUser.getId());
            User existingUser = userMapper.selectOne(checkQuery);
            if (existingUser != null) {
                throw new IllegalArgumentException("账号已被占用");
            }
        }
        // 4. 处理头像上传 (如果提供了文件)
        String newAvatarUrl = null;
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                // 4a. 验证文件类型和大小 (参考 FileUploadController)
                String contentType = avatarFile.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new IllegalArgumentException("头像必须是图片文件");
                }
                long maxSize = 5 * 1024 * 1024; // 5MB
                if (avatarFile.getSize() > maxSize) {
                    throw new IllegalArgumentException("头像文件大小不能超过5MB");
                }

                // 4b. 创建上传目录 (如果不存在)
                Path uploadDir = Paths.get(uploadPath);
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                // 4c. 生成唯一文件名
                String originalFilename = avatarFile.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
                }
                if (fileExtension.isEmpty()) {
                    fileExtension = ".jpg"; // 默认扩展名
                }
                String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

                // 4d. 保存文件到指定目录
                Path filePath = uploadDir.resolve(uniqueFilename);
                Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // 4e. 构造新的头像访问URL
                // 4e. 构造新的头像访问URL - 修改此处
                // 获取当前请求的 HttpServletRequest 对象
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                String scheme = request.getScheme(); // "http" or "https"
                String serverName = request.getServerName(); // "49.233.45.219"
                int serverPort = request.getServerPort(); // 8080
                String contextPath = request.getContextPath(); // 通常是 "" 或 "/your-app-name"

                // 构造基础 URL
                StringBuilder baseUrl = new StringBuilder(scheme).append("://").append(serverName);
                if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
                    baseUrl.append(":").append(serverPort);
                }
                baseUrl.append(contextPath);

                // 确保 accessPath 以 "/" 开头
                String finalAccessPath = accessPath.startsWith("/") ? accessPath : "/" + accessPath;
                // 确保 finalAccessPath 不以 "/" 结尾，以避免双斜杠
                if (finalAccessPath.endsWith("/")) {
                    finalAccessPath = finalAccessPath.substring(0, finalAccessPath.length() - 1);
                }
                // 拼接最终的完整 URL
                newAvatarUrl = baseUrl.toString() + finalAccessPath + "/" + uniqueFilename;

                System.out.println("头像上传成功，保存路径: " + filePath.toString());
                System.out.println("新头像完整URL: " + newAvatarUrl);

            } catch (IOException e) {
                throw new RuntimeException("头像上传失败", e);
            }
        }
        // 5. 更新用户信息到数据库
        User updateUser = new User();
        updateUser.setId(currentUser.getId()); // 设置ID以更新特定用户
        updateUser.setName(newName);
        updateUser.setZhanghao(newZhanghao);
        // 如果上传了新头像，则更新URL；否则保留原URL
        if (newAvatarUrl != null) {
            updateUser.setAvatarurl(newAvatarUrl);
        } else {
            // 如果没有上传新头像，不更新avatarurl字段，或者保留旧值
            // MyBatis-Plus的UpdateWrapper可以处理null值不更新，或者你可以显式设置为原值
            // 这里我们不显式设置avatarurl，除非newAvatarUrl不为null
            // 如果你想确保旧值不变，可以在这里获取currentUser.getAvatarurl()并设置
            // 但通常情况下，如果字段为null，MyBatis-Plus不会更新它（取决于配置）
            // 为了安全起见，我们显式处理：
            if (newAvatarUrl == null) {
                // 不设置avatarurl，保持原值
            } else {
                updateUser.setAvatarurl(newAvatarUrl);
            }
            // 更简洁的方式是，只在newAvatarUrl非null时才设置它
        }
        // 注意：这里不更新 password 和 creattime

        int updateResult = userMapper.updateById(updateUser);
        if (updateResult <= 0) {
            throw new RuntimeException("更新用户信息失败");
        }

        // 6. 返回更新后的用户信息 (不含密码)
        User updatedUser = userMapper.selectById(currentUser.getId());
        updatedUser.setPassword(null); // 不返回密码
        return updatedUser;
    }
}




