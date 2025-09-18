package com.nsc.ipfind.controller;

import com.nsc.ipfind.dto.LoginRequest;
import com.nsc.ipfind.pojos.User;
import com.nsc.ipfind.service.UserService;
import com.nsc.ipfind.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        User user = userService.getUserByUsername(username);
        if (user == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "用户名错误");
            return ResponseEntity.status(401).body(errorResponse);
        }

        if (!password.equals(user.getPassword())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "密码错误");
            return ResponseEntity.status(401).body(errorResponse);
        }

        // 登录成功逻辑不变
        String token = jwtUtil.generateToken(user.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());
        response.put("name", user.getName());
        response.put("id", user.getId());

        return ResponseEntity.ok(response);
    }
    //注册
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.save(user)) {
            return ResponseEntity.ok("注册成功");
        } else {
            return ResponseEntity.status(500).body("注册失败");
        }
    }
}
