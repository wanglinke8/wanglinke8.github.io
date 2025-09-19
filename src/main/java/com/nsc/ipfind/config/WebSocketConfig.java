package com.nsc.ipfind.config; // 请替换为你的实际包名

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker // 启用 WebSocket 消息代理
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private HandshakeInterceptor handshakeInterceptor; // 注入拦截器

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .addInterceptors(handshakeInterceptor) // 添加拦截器
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * 配置消息代理
     * 定义了消息在客户端和服务器之间往返的路径前缀。
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 配置一个简单的基于内存的消息代理，用于将消息广播到订阅了特定前缀的主题的客户端
        // 例如，客户端订阅 "/topic/messages"，服务器可以向 "/topic/messages" 发送消息
        registry.enableSimpleBroker("/topic", "/queue");

        // 配置应用程序的目的地前缀。
        // 以 "/app" 开头的消息会被路由到 @MessageMapping 注解的方法（即我们的控制器方法）
        // 例如，客户端向 "/app/chat" 发送消息，会路由到处理 "/chat" 的 @MessageMapping 方法
        registry.setApplicationDestinationPrefixes("/app");
    }
}
