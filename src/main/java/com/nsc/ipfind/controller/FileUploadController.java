package com.nsc.ipfind.controller;

import com.nsc.ipfind.util.JwtUtil;
import com.nsc.ipfind.service.UserService;
import com.nsc.ipfind.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // 从配置文件中读取上传路径
    @Value("${file.upload.path:uploads/}")
    private String uploadPath;

    // 从配置文件中读取访问路径前缀
    @Value("${file.access.path:/uploads/}")
    private String accessPath;

    @PostMapping("/image")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 验证 Token 并获取当前用户信息
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("code", 401);
                response.put("message", "未提供有效的认证信息");
                return ResponseEntity.status(401).body(response);
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                response.put("code", 401);
                response.put("message", "无效或已过期的认证令牌");
                return ResponseEntity.status(401).body(response);
            }

            String currentZhanghao = jwtUtil.getZhanghaoFromToken(token);
            if (currentZhanghao == null || currentZhanghao.isEmpty()) {
                response.put("code", 401);
                response.put("message", "令牌中未包含有效的用户信息");
                return ResponseEntity.status(401).body(response);
            }

            User currentUser = userService.getUserByUsername(currentZhanghao);
            if (currentUser == null) {
                response.put("code", 404);
                response.put("message", "当前用户信息不存在");
                return ResponseEntity.status(404).body(response);
            }

            // 2. 验证文件
            if (file.isEmpty()) {
                response.put("code", 400);
                response.put("message", "上传文件不能为空");
                return ResponseEntity.status(400).body(response);
            }

            // 3. 检查文件类型（只允许图片）
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                response.put("code", 400);
                response.put("message", "只允许上传图片文件");
                return ResponseEntity.status(400).body(response);
            }

            // 4. 检查文件大小（例如限制为5MB）
            long maxSize = 5 * 1024 * 1024; // 5MB
            if (file.getSize() > maxSize) {
                response.put("code", 400);
                response.put("message", "文件大小不能超过5MB");
                return ResponseEntity.status(400).body(response);
            }

            // 5. 创建上传目录（如果不存在）
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 6. 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            }

            // 确保有正确的文件扩展名
            if (fileExtension.isEmpty()) {
                fileExtension = ".jpg"; // 默认扩展名
            }

            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // 7. 保存文件到指定目录
            Path filePath = uploadDir.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 8. 构造访问URL - 确保路径格式正确
            String fileUrl;
            if (accessPath.endsWith("/")) {
                fileUrl = accessPath + uniqueFilename;
            } else {
                fileUrl = accessPath + "/" + uniqueFilename;
            }

            System.out.println("文件上传成功，保存路径: " + filePath.toString());
            System.out.println("访问URL: " + fileUrl);

            // 9. 返回成功响应
            response.put("code", 200);
            response.put("message", "图片上传成功");
            response.put("data", fileUrl);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            response.put("code", 500);
            response.put("message", "文件上传失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", 500);
            response.put("message", "服务器内部错误: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
