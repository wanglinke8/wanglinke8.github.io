package com.nsc.ipfind.controller;

import com.nsc.ipfind.pojos.User;
import com.nsc.ipfind.service.UserService;
import com.nsc.ipfind.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest; // Spring Boot 2.x
// import jakarta.servlet.http.HttpServletRequest; // Spring Boot 3.x, 请根据你的版本选择
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
    private JwtUtil jwtUtil; // 注入你的 JwtUtil

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listUsers(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 获取 Token
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("code", 401);
                response.put("message", "未提供有效的认证信息");
                return ResponseEntity.status(401).body(response);
            }
            String token = authHeader.substring(7); // 移除 "Bearer "

            // --- 关键修改点 ---
            // 2. 验证 Token 并获取其中的 zhanghao (即 getUsernameFromToken 的返回值)
            //    这里我们先验证，再获取，确保安全性
            if (!jwtUtil.validateToken(token)) { // 先验证 Token 有效性
                response.put("code", 401);
                response.put("message", "无效或已过期的认证令牌");
                return ResponseEntity.status(401).body(response);
            }
            String currentZhanghao = jwtUtil.getZhanghaoFromToken(token); // 获取 zhanghao
            // --- 关键修改点结束 ---

            if (currentZhanghao == null || currentZhanghao.isEmpty()) {
                response.put("code", 401);
                response.put("message", "令牌中未包含有效的用户信息");
                return ResponseEntity.status(401).body(response);
            }

            // 3. 根据账号获取当前用户完整信息
            User currentUser = userService.getUserByUsername(currentZhanghao);
            if (currentUser == null) {
                response.put("code", 404);
                response.put("message", "当前用户信息不存在");
                return ResponseEntity.status(404).body(response);
            }

            // 4. 查询所有用户
            List<User> allUsers = userService.list(); // 假设继承了 MyBatis Plus IService

            // 5. 过滤掉当前用户自己
            List<User> otherUsers = allUsers.stream()
                    .filter(user -> !user.getId().equals(currentUser.getId()))
                    .collect(Collectors.toList());

            // 6. 构造成功响应
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
}
