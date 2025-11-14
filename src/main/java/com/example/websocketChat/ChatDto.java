package com.example.websocketChat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDto {

    private String type;      // chat, join, leave 등
    private String roomId;    // default-room
    private String sender;    // web, wpf, user1 등
    private String content;   // 메시지 본문
    public ChatDto(String type, String roomId, String sender, String content)
    {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
    }
    public   ChatDto(){}
}
