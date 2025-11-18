package com.example.websocketChat;



import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
public class WebsocketHandler extends TextWebSocketHandler {

    private ObjectMapper objectMap;
    private  RoomManager  RoomM;
  public WebsocketHandler(ObjectMapper objectMapper, RoomManager RoomM) {
    this.objectMap = objectMapper;
    this.RoomM = RoomM;
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
                    Createhandle (session,chatDto );
                break;
                case "Join":
                    Jojnhandle (session,chatDto );
                    break;
                    case "Leave":
                        leavehandle ( session, chatDto);
                        break;
                        case "Chat":
                            handletoChat(chatDto);
                            break;
                default:
                    SendError(session,"알 수 없는 type: " +  chatDto.getType());
                    break;
            }

        }
        catch (WebClientResponseException ex) {
            System.out.println("=== WebClientResponseException ===");
            System.out.println("Status: " + ex.getStatusCode());
            System.out.println("Body  : " + ex.getResponseBodyAsString());
            ex.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
   }
   private void broadcast(String roomid ,ChatDto dt) throws IOException {
       //자바 객체 → JSON 문자열(String)으로 변환하는 함수
       String id = objectMap.writeValueAsString(dt);
       Set<WebSocketSession> LI = RoomM.getRoominfoSeesion(roomid);
       if( LI != null &&LI.size()>0) {
           for (WebSocketSession s : LI)
               s.sendMessage(new TextMessage(id));
       }
   }
   private void handletoChat(ChatDto dto) throws IOException
   {
        String rid =   dto.getRoomId();
        log.info(rid);
        if(rid.isBlank()||rid ==null)
        {
            log.warn("방제목 없음");
            return;
        }
        broadcast(rid ,dto);
   }
   private void Jojnhandle (WebSocketSession session,ChatDto dto) throws IOException
   {
       String roomid = dto.getRoomId();
       String senderid = dto.getSender();
       if(roomid.isBlank()||roomid==null)
       {
           log.warn("방제목 없음");
           return;
       }
       RoomM.Addroomorjoinroom(roomid,session);
       ChatDto dto1 =  new  ChatDto(
               "system",
               roomid,
               "server",
                senderid+"님이 방에 참가하였습니다"
       );
       RoomM.Addsession(roomid,senderid,session);
       log.info(dto1.toString());
       broadcast(roomid,dto1);
       ChatDto dto2 =  new  ChatDto(
               "Join",
               roomid,
               "server",
               ""
       );
       String json = objectMap.writeValueAsString(dto2);
       session.sendMessage(new TextMessage(json));
   }
   private  void Createhandle (WebSocketSession session,ChatDto dto)  throws IOException
   {
       String roomid = dto.getRoomId();
       String senderid = dto.getSender();
       if(roomid.isBlank()||roomid==null)
           return;
       if( RoomM.getRoominfo().contains(roomid))
       {
           log.warn("방있음");
           return;
       }

       RoomM.Addroomorjoinroom(roomid,session);
       RoomM.Addsession(roomid,senderid,session);
       ChatDto dto1 =  new  ChatDto(
               "Create",
               roomid,
               "server",
               ""
       );
       String json = objectMap.writeValueAsString(dto1);
       session.sendMessage(new TextMessage(json));
   }
   private  void leavehandle (WebSocketSession session,ChatDto dto)  throws IOException
   {
       String roomid = dto.getRoomId();
       String senderid = dto.getSender();
       if(roomid.isBlank()||roomid==null)
       {
           log.warn("방제목 없음");
           return;
       }
       ChatDto dto1 =  new  ChatDto(
               "Leave",
               roomid,
               "server",
               senderid+"님이 방에 나가셨습니다"
       );
       RoomM.leavesession(roomid,session);
       broadcast(roomid ,dto1);
       String json = objectMap.writeValueAsString(dto1);
       session.sendMessage(new TextMessage(json));
   }
    private void SendError(WebSocketSession session, String message) throws IOException
    {
        ChatDto chatdt= new ChatDto(
                "error",
                null,
                "Sever",
                message);
        String Json = objectMap.writeValueAsString(chatdt);
        if(session.isOpen())
        {
            session.sendMessage(new TextMessage(Json));
        }
    }
}
