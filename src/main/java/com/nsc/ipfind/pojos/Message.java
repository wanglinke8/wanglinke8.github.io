package com.nsc.ipfind.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 *
 * @TableName message
 */
@TableName(value ="message")
@Data
public class Message implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO) // 指定主键及生成策略为自增
    private Integer id;

    /**
     * 发送者ID (外键关联 user.id)
     */
    @TableField(value = "sender_id") // 明确指定数据库列名
    private Integer senderId;

    /**
     * 接收者ID (外键关联 user.id)
     */
    @TableField(value = "receiver_id") // 明确指定数据库列名
    private Integer receiverId;

    /**
     * 消息内容
     */
    @TableField(value = "content") // 明确指定数据库列名
    private String content;


    /**
     * 消息类型
     */
    @TableField(value = "message_type")
    private String messageType;

    /**
     * 消息时间戳
     */
    @TableField(value = "timestamp") // 明确指定数据库列名
    private Date timestamp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    // 保持默认的无参构造函数（Lombok @Data 会自动生成）
    // 如果需要有参构造函数，可以显式添加

    // 显式添加无参构造函数（确保存在）
    public Message() {
        this.messageType = "TEXT"; // 默认值
    }

    // 可选：添加有参构造函数
    public Message(Integer senderId, Integer receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.messageType = "TEXT"; // 默认值
    }

    // 可选：添加包含消息类型的构造函数
    public Message(Integer senderId, Integer receiverId, String content, String messageType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.messageType = messageType != null ? messageType : "TEXT";
    }
}
