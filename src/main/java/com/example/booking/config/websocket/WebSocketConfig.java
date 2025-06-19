package com.example.booking.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint cho client kết nối, sử dụng SockJS để hỗ trợ fallback
        // Client sẽ connect tới ws://{host}:{port}/ws-seat
        // và sử dụng SockJS để fallback khi WebSocket không khả dụng.
        registry.addEndpoint("/ws-ticket")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Dùng simple in-memory broker: tất cả destination bắt đầu /topic/** sẽ broadcast
        registry.enableSimpleBroker("/topic");
        // Client gửi message đến các @MessageMapping có prefix /app
        registry.setApplicationDestinationPrefixes("/app");
        // Nếu muốn gửi message chỉ cho một session/user: prefix /user (nếu xài)
        registry.setUserDestinationPrefix("/user");
    }
}
