package com.nsc.ipfind.controller;

import com.nsc.ipfind.dto.ChatMessage;
import com.nsc.ipfind.pojos.Message;
import com.nsc.ipfind.service.MessageService;
import com.nsc.ipfind.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String zhanghao = (String) headerAccessor.getSessionAttributes().get("zhanghao");

        if (zhanghao != null && !zhanghao.isEmpty()) {
            chatMessage.setSender(zhanghao);

            System.out.println("Authenticated user sending message: " + zhanghao);

            // --- 新增：保存消息到数据库 ---
            try {
                Message dbMessage = new Message();
                // 将 ChatMessage 的属性复制到 Message 实体
                // 注意：需要确保字段名匹配或手动设置
                dbMessage.setSenderId(chatMessage.getSenderId());
                dbMessage.setReceiverId(chatMessage.getReceiverId());
                dbMessage.setContent(chatMessage.getContent());
                // timestamp 由数据库自动设置 CURRENT_TIMESTAMP，或者你可以在这里设置 new Date()
                // dbMessage.setTimestamp(new Date());

                boolean isSaved = messageService.save(dbMessage);
                if (isSaved) {
                    System.out.println("Message saved to database: " + dbMessage.getId());
                    // (可选) 将数据库生成的 ID 和 timestamp 更新到 chatMessage 对象，
                    // 然后发送给客户端，这样客户端可以更新本地显示的消息 ID
                    // chatMessage.setId(dbMessage.getId());
                    // chatMessage.setTimestamp(dbMessage.getTimestamp());
                } else {
                    System.err.println("Failed to save message to database.");
                }
            } catch (Exception e) {
                System.err.println("Error saving message to database: " + e.getMessage());
                e.printStackTrace();
            }
            // --- 新增结束 ---

            // 构造发送给接收者的主题路径
            String recipientTopic = "/topic/messages/user/" + chatMessage.getReceiverId();
            messagingTemplate.convertAndSend(recipientTopic, chatMessage);

        } else {
            System.out.println("Unauthorized message send attempt.");
        }
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String zhanghao = (String) headerAccessor.getSessionAttributes().get("zhanghao");
        if (zhanghao != null && !zhanghao.isEmpty()) {
            chatMessage.setSender(zhanghao);
            System.out.println("User joined (authenticated): " + zhanghao);
            // 可以广播加入消息
            // messagingTemplate.convertAndSend("/topic/public", chatMessage);
        } else {
            System.out.println("Anonymous user joined.");
        }
    }
}
