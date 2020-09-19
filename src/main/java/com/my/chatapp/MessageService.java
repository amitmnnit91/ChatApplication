package com.my.chatapp;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.my.chatapp.MessageDTO.Type;

@Service
public class MessageService {
	
	@Autowired
    private SimpMessageSendingOperations messagingTemplate;

	public void sendMessage(Type type, String topic, String content, String userId) {
		if(CacheManager.isActive(userId)) {
			messagingTemplate.convertAndSend("/topic/"+topic, new MessageTimeStampDTO(type, content, userId, Instant.now().toEpochMilli()));
		}
		if(type.equals(Type.DELIVER)){
			messagingTemplate.convertAndSendToUser(userId, "/topic/"+topic, new MessageTimeStampDTO(Type.SENT, "", userId, Instant.now().toEpochMilli()));
		}
		CacheManager.addMessagesToTopic(type, topic, content, userId);
	}
}
