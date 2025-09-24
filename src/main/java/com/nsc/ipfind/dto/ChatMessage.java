package com.nsc.ipfind.dto;

import lombok.Data;

@Data // Lombok 注解
public class ChatMessage {

    public enum MessageType {
        CHAT,       // 聊天消息
        JOIN,       // 用户加入
        LEAVE,       // 用户离开
        IMAGE,       // 图片消息
        VIDEO_CALL_OFFER,     // 视频通话 Offer
        VIDEO_CALL_ANSWER,    // 视频通话 Answer
        VIDEO_CALL_ICE_CANDIDATE, // ICE 候选
        VIDEO_CALL_HANGUP     // 挂断
    }

    private MessageType type; // 消息类型
    private String content;   // 消息内容
    private String sender;    // 发送者名称 (或 zhanghao)
    private Integer senderId; // 发送者ID
    private Integer receiverId; // 接收者ID (用于私聊)

    // WebRTC 相关字段
    private Object offer;      // SDP Offer
    private Object answer;     // SDP Answer
    private Object candidate;  // ICE Candidate

    private String messageType;
}
