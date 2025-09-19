package com.nsc.ipfind.dto; // 或者 com.nsc.ipfind.dto

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data // Lombok 注解，自动生成 getter, setter, toString 等
public class SendMessageRequest {

    /**
     * 接收者ID
     */
    @NotNull(message = "接收者ID不能为空")
    private Integer receiverId;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    private String content;

    // Lombok @Data 会处理 getter/setter
}
