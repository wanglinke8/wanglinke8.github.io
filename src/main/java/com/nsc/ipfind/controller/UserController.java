package com.nsc.ipfind.controller;

import com.nsc.ipfind.pojos.User;
import com.nsc.ipfind.service.UserService;
import com.nsc.ipfind.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listUsers(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
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

            List<User> allUsers = userService.list();

            List<User> otherUsers = allUsers.stream()
                    .filter(user -> !user.getId().equals(currentUser.getId()))
                    .collect(Collectors.toList());

            response.put("code", 200);
            response.put("message", "获取成功");
            response.put("data", otherUsers);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", 500);
            response.put("message", "服务器内部错误: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    //更新用户信息接口
    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(
            @RequestHeader("Authorization") String authHeader, // 从请求头获取Token
            @RequestParam("name") String newName,
            @RequestParam("zhanghao") String newZhanghao,
            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile // 文件是可选的
    ) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "未提供认证Token或格式错误");
                return ResponseEntity.status(401).body(errorResponse);
            }
            String token = authHeader.substring(7); // 去掉 "Bearer " 前缀

            // 调用服务层更新用户信息
            User updatedUser = userService.updateUserInfo(token, newName, newZhanghao, avatarFile);

            // 更新成功，返回新信息
            Map<String, Object> response = new HashMap<>();
            response.put("message", "资料更新成功");
            response.put("name", updatedUser.getName());
            response.put("zhanghao", updatedUser.getZhanghao());
            response.put("id", updatedUser.getId());
            response.put("avatarurl", updatedUser.getAvatarurl()); // 返回新的头像URL
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // 处理业务逻辑错误 (如账号重复、用户不存在、文件类型错误等)
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(400).body(errorResponse); // 400 Bad Request
        } catch (RuntimeException e) {
            // 处理运行时错误 (如文件上传失败等)
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse); // 500 Internal Server Error
        } catch (Exception e) {
            // 处理其他未预期的错误
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "服务器内部错误");
            e.printStackTrace(); // 记录错误日志
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
