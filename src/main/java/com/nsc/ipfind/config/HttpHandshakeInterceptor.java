package com.nsc.ipfind.config;

import com.nsc.ipfind.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        // 从握手请求中获取 Token
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String authToken = servletRequest.getServletRequest().getParameter("token"); // 从查询参数获取

            if (authToken != null && authToken.startsWith("Bearer ")) {
                authToken = authToken.substring(7); // 移除 "Bearer "
            }

            if (authToken != null && jwtUtil.validateToken(authToken)) {
                String zhanghao = jwtUtil.getZhanghaoFromToken(authToken);
                attributes.put("zhanghao", zhanghao); // 将 zhanghao 存入 attributes，在 WebSocketSession 中可以获取
                System.out.println("WebSocket handshake authenticated user: " + zhanghao);
                return true; // 允许握手
            } else {
                System.out.println("WebSocket handshake failed: Invalid token");
            }
        }
        // 如果没有有效 token，可以选择拒绝连接 (返回 false)
        // 或者允许连接，但在后续消息中验证 (返回 true)
        // 这里我们为了演示，暂时允许连接，但不设置用户属性
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 握手后执行的操作（如果需要）
    }
}
