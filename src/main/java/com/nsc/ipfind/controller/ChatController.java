package com.nsc.ipfind.controller;

import com.nsc.ipfind.dto.ChatMessage;
import com.nsc.ipfind.pojos.Message;
import com.nsc.ipfind.service.MessageService;
import com.nsc.ipfind.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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
    public void sendMessage(@Payload ChatMessage chatMessage) {
        try {
            System.out.println("收到WebSocket消息: " + chatMessage);

            // 保存消息到数据库
            if (chatMessage.getSenderId() != null && chatMessage.getReceiverId() != null) {
                Message message = new Message();
                message.setSenderId(chatMessage.getSenderId());
                message.setReceiverId(chatMessage.getReceiverId());
                message.setContent(chatMessage.getContent());

                // 设置消息类型
                String messageType = chatMessage.getMessageType();
                if (messageType == null || messageType.isEmpty()) {
                    // 根据内容判断消息类型
                    if (chatMessage.getContent() != null &&
                            chatMessage.getContent().startsWith("/uploads/")) {
                        message.setMessageType("IMAGE");
                    } else {
                        message.setMessageType("TEXT");
                    }
                } else {
                    message.setMessageType(messageType);
                }

                // 保存到数据库
                boolean isSaved = messageService.save(message);
                if (isSaved) {
                    System.out.println("消息已保存到数据库: " + message.getId());
                } else {
                    System.err.println("消息保存失败");
                }
            }

            // 发送消息给接收者
            String destination = "/topic/messages/user/" + chatMessage.getReceiverId();
            messagingTemplate.convertAndSend(destination, chatMessage);
            System.out.println("消息已发送到: " + destination);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("处理WebSocket消息时出错: " + e.getMessage());
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
