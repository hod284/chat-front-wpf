package com.example.websocketChat;



import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.logging.Log;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class WebsocketHandler extends TextWebSocketHandler {

    private ObjectMapper objectMap;

  public WebsocketHandler(ObjectMapper objectMapper) {
    this.objectMap = objectMapper;
  }
   @Override// 연결된후
   public void afterConnectionEstablished(WebSocketSession session ) throws Exception
   {
       log.info("웹소켓 커넷팅 완료");
   }
   @Override // 에로 났을때
    public void handleTransportError(WebSocketSession session , Throwable exception) throws Exception
   {
       log.warn("웹소켓 에러" + exception.getMessage());
   }
   @Override// 웹소켓 닫았을때
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws  Exception
   {
       super.afterConnectionClosed(session, status);
   }
   @Override // 메세지 다루는 부분
    public void  handleTextMessage(WebSocketSession session , TextMessage message) throws Exception
   {
        try
        {
            String msg = message.getPayload();
            log.info(msg);
            ChatDto chatDto = objectMap.readValue(msg, ChatDto.class);
            log.info(chatDto.toString());
            switch (chatDto.getType())
            {
                case "Create":

                break;
                case "Join":
                    break;
                    case "Leave":
                        break;
                        case "Chat":
                            break;
            }

        }
       catch (Exception e)
       {

       }
   }
   private void broadcast(String roomid ,ChatDto dt) {
       //자바 객체 → JSON 문자열(String)으로 변환하는 함수
       //String id = objectMap.writeValueAsString(dt);
   }
}
