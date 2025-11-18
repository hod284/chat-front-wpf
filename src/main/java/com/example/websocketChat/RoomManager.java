package com.example.websocketChat;


import jakarta.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class RoomManager {
   private final Map<String, Set<WebSocketSession>> Roominfo = new ConcurrentHashMap<>();
   private final Map<WebSocketSession, Set<Sessionifo>> Sessioninfolist = new ConcurrentHashMap<>();
   public Set<String> getRoominfo()
   {
       return Roominfo.keySet();
   }

   public Set<WebSocketSession> getRoominfoSeesion(String username)
   {
       return Roominfo.get(username);
   }
   public void RemoveRoom(String Roomname)
   {
       Roominfo.remove(Roomname);
   }
   public void Addroomorjoinroom(String roomid,WebSocketSession session)
   {
       // putIfAbsent() 메서드는 네이밍에서 유추할 수 있듯이, 다음과 같이 동작합니다.

       //Key가 존재할 경우: Value 변경 없이 기존에 존재하는 Key의 Value를 리턴합니다.
       //Key가 존재하지 않는 경우: Key에 해당하는 Value를 저장한 후 null을 리턴합니다.
       //id->ConcurrentHashMap.newKeySet() 동시 접근(멀티스레드)에 안전한 Set 하나 만들어서 반환해줌
       Roominfo.computeIfAbsent(roomid,id->ConcurrentHashMap.newKeySet()).add(session);
       log.info(Roominfo.keySet().toString());
   }
    public void Addsession(String roomid,String sender,WebSocketSession session)
    {
        Sessionifo se= new Sessionifo(
                roomid,
                sender
        );
        Sessioninfolist.computeIfAbsent(session,id->ConcurrentHashMap.newKeySet()).add(se);
        log.info(Sessioninfolist.keySet().toString());
    }
    public void leavesession(String roomid,WebSocketSession session)
    {
        // putIfAbsent() 메서드는 네이밍에서 유추할 수 있듯이, 다음과 같이 동작합니다.

        //Key가 존재할 경우: Value 변경 없이 기존에 존재하는 Key의 Value를 리턴합니다.
        //Key가 존재하지 않는 경우: Key에 해당하는 Value를 저장한 후 null을 리턴합니다.
        //id->ConcurrentHashMap.newKeySet() 동시 접근(멀티스레드)에 안전한 Set 하나 만들어서 반환해줌

        var Value = Roominfo.get(roomid);
            if(Value == null||Value.size()==0)
                RemoveRoom(roomid);
            else  if(Roominfo.size()>0)
            {
                Roominfo.get(roomid).remove(session);
                Value = Roominfo.get(roomid);
                if(Value == null||Value.size()==0)
                RemoveRoom(roomid);
            }

        if(Sessioninfolist.size()>0)
           Sessioninfolist.get(session).removeIf(x->x.getRoomId().equals(roomid));
    }
}
