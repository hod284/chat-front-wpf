package com.example.websocketChat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.Console;

@Slf4j
@SpringBootApplication
public class WebsocketChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketChatApplication.class, args);
         log.info("WebsocketChatApplication started");
	}
}
