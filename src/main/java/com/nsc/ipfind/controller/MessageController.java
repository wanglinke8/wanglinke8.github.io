package com.nsc.ipfind.controller;


import com.nsc.ipfind.dto.SendMessageRequest;
import com.nsc.ipfind.pojos.Message;
import com.nsc.ipfind.pojos.User;
import com.nsc.ipfind.service.MessageService;
import com.nsc.ipfind.service.UserService;
import com.nsc.ipfind.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * 消息相关接口控制器
 */
@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*") // 根据你的前端部署情况配置 CORS，这里暂时允许所有
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取与指定用户的聊天历史记录
     * 请求路径: GET /api/messages/history
     * 请求参数: targetUserId (通过 URL 查询参数传递)
     */
    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getChatHistory(
            @RequestParam("targetUserId") Integer targetUserId,
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

            Integer currentUserId = currentUser.getId();

//            Integer currentUserId = currentUser.getId();
// --- 添加调试信息 ---
            System.out.println("DEBUG: Current User ID from Token/Zhanghao (" + currentZhanghao + "): " + currentUserId);
            System.out.println("DEBUG: Target User ID from Request Param: " + targetUserId);

            // 2. 验证目标用户ID是否有效
            if (targetUserId == null || targetUserId.equals(currentUserId)) {
                response.put("code", 400);
                response.put("message", "无效的目标用户ID");
                return ResponseEntity.status(400).body(response);
            }
            // 可选：检查 targetUserId 对应的用户是否存在
            User targetUser = userService.getById(targetUserId);

            if (targetUser == null) {
                // 如果严格要求，可以返回错误；也可以允许，因为可能是历史消息
                // 这里选择允许，因为消息可能存在，即使对方账号被删除（根据外键策略）
                // response.put("code", 404);
                // response.put("message", "目标用户不存在");
                // return ResponseEntity.status(404).body(response);
            }


            // 3. 查询聊天历史记录
            // 查询条件：(sender_id = currentUserId AND receiver_id = targetUserId)
            //        OR (sender_id = targetUserId AND receiver_id = currentUserId)
            // 按时间戳升序排列
            List<Message> chatHistory = messageService.lambdaQuery()
                    // 第一组条件: (sender_id = currentUserId AND receiver_id = targetUserId)
                    .nested(w1 -> w1.eq(Message::getSenderId, currentUserId)
                            .eq(Message::getReceiverId, targetUserId))
                    // OR
                    .or()
                    // 第二组条件: (sender_id = targetUserId AND receiver_id = currentUserId)
                    .nested(w2 -> w2.eq(Message::getSenderId, targetUserId)
                            .eq(Message::getReceiverId, currentUserId))
                    .orderByAsc(Message::getTimestamp)
                    .list();


            System.out.println("DEBUG: Queried chat history size: " + (chatHistory != null ? chatHistory.size() : "null"));
            if (chatHistory != null && !chatHistory.isEmpty()) {
                System.out.println("DEBUG: First message content: " + chatHistory.get(0).getContent());
            }
            // 4. 构造成功响应
            response.put("code", 200);
            response.put("message", "获取成功");
            response.put("data", chatHistory);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace(); // 实际项目中应使用日志框架记录
            response.put("code", 500);
            response.put("message", "服务器内部错误: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendMessage(
            @Valid @RequestBody SendMessageRequest request, // 使用 DTO 接收请求体，并进行验证
            HttpServletRequest httpRequest) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 验证 Token 并获取当前用户信息 (发送者)
            String authHeader = httpRequest.getHeader("Authorization");
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

            String currentZhanghao = jwtUtil.getZhanghaoFromToken(token); // 使用修改后的方法名
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

            Integer senderId = currentUser.getId();

            // 2. 获取请求数据
            Integer receiverId = request.getReceiverId();
            String content = request.getContent();

            // 3. 基本验证 (DTO 的 @Valid 会处理部分内容)
            if (receiverId == null || receiverId.equals(senderId)) {
                response.put("code", 400);
                response.put("message", "无效的接收者ID");
                return ResponseEntity.status(400).body(response);
            }
            if (content == null || content.trim().isEmpty()) {
                // 这个检查其实 DTO 的 @NotBlank 已经做了，但保留作为示例
                response.put("code", 400);
                response.put("message", "消息内容不能为空");
                return ResponseEntity.status(400).body(response);
            }

            // 4. (可选) 检查接收者是否存在
            User receiverUser = userService.getById(receiverId);
            if (receiverUser == null) {
                response.put("code", 404);
                response.put("message", "接收者用户不存在");
                return ResponseEntity.status(404).body(response);
            }

            // 5. 创建消息对象并保存
            Message message = new Message();
            message.setSenderId(senderId);
            message.setReceiverId(receiverId);
            message.setContent(content.trim()); // 保存前去除首尾空格
            // timestamp 会由数据库自动设置为 CURRENT_TIMESTAMP

            boolean isSaved = messageService.save(message);

            if (isSaved) {
                response.put("code", 200);
                response.put("message", "消息发送成功");
                // 可以选择返回刚保存的消息对象，供前端立即显示
                response.put("data", message);
                return ResponseEntity.ok(response);
            } else {
                // 保存失败比较少见，但理论上可能发生（如数据库连接问题）
                response.put("code", 500);
                response.put("message", "消息保存失败");
                return ResponseEntity.status(500).body(response);
            }


        } catch (Exception e) {
            e.printStackTrace(); // 实际项目中应使用日志框架记录
            response.put("code", 500);
            response.put("message", "服务器内部错误: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
