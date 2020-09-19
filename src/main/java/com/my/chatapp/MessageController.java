package com.my.chatapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MessageController {
	
	@Autowired
	private MessageService msgService;

	@MessageMapping("/chat/{topic}/msg")
	public void message(@DestinationVariable String topic, @Payload MessageDTO dto) throws Exception {
		msgService.sendMessage(dto.getType(), topic, dto.getContent(), dto.getUserId());
	}
	
	@MessageMapping("/chat/add/{userId}")
	public void newUser(@DestinationVariable String userId,SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", userId);
		CacheManager.activateUser(userId);
	}
	
	@RequestMapping("/app/{topic}/msgs")
	public ResponseEntity<List<MessageTimeStampDTO>> getAllMessages(@PathVariable String topic){
		return new ResponseEntity<List<MessageTimeStampDTO>>(CacheManager.getAllMessagesFromTS(topic, 0), HttpStatus.OK);
	}

}
