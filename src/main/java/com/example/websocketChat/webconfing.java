package com.example.websocketChat;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class webconfing  implements WebSocketConfigurer {

    private WebsocketHandler websocketHandler;
    public webconfing(WebsocketHandler websocket) {
        this.websocketHandler = websocket;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(websocketHandler, "/ws/chat")
                .setAllowedOrigins("*");
    }


}
