package com.xcrj.ws.spring.config;

import com.xcrj.ws.spring.service.WebSocket;
import com.xcrj.ws.spring.service.impl.WebSocketImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
// 开启WebSocket功能
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Bean
    public DefaultWebSocketHandler defaultWebSocketHandler() {
        return new DefaultWebSocketHandler();
    }

    @Bean
    public WebSocket webSocket() {
        return new WebSocketImpl();
    }

    @Bean
    public WebSocketInterceptor webSocketInterceptor() {
        return new WebSocketInterceptor();
    }

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        // 添加处理器
        registry.addHandler(defaultWebSocketHandler(), "ws/spring")
                // 添加拦截器
                .addInterceptors(webSocketInterceptor())
                // 设置允许跨域（允许所有请求来源）
                .setAllowedOrigins("*");
    }
}
