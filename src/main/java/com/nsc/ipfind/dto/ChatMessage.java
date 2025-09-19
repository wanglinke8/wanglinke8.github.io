package com.nsc.ipfind.dto;

import lombok.Data;

@Data // Lombok 注解
public class ChatMessage {

    public enum MessageType {
        CHAT,       // 聊天消息
        JOIN,       // 用户加入
        LEAVE       // 用户离开
        // 可以根据需要添加更多类型，如 TYPING
    }

    private MessageType type; // 消息类型
    private String content;   // 消息内容
    private String sender;    // 发送者名称 (或 zhanghao)
    private Integer senderId; // 发送者ID
    private Integer receiverId; // 接收者ID (用于私聊)
    // 可以添加时间戳等其他字段
}
