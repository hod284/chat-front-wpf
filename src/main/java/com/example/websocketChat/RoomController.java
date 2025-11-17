package com.example.websocketChat;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/Rooms")
public class RoomController {
    private final  RoomManager RoomM;
    public RoomController(RoomManager roomManager)
    {
        RoomM = roomManager;
    }
    @GetMapping
    public ResponseEntity<Set<String>> GetRoomsInfoList()
    {
        Set<String> RoomsInfo = RoomM.getRoominfo();
        return  ResponseEntity.ok(RoomsInfo);
    }
}
