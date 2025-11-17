package com.example.websocketChat;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
public class Sessionifo {

    private String roomId;    // default-room
    private String sender;
    public Sessionifo(String roomId, String sender) {
        this.roomId = roomId;
        this.sender = sender;
    }

}
