package com.my.chatapp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.my.chatapp.MessageDTO.Type;

public class CacheManager {
	
	private static Set<String> activeUsers = new HashSet<String>();
	
	private static Map<String, Long> userToLastActiveMap = new HashMap<String, Long>();
	
	private static Set<String> disconnectedUsers = new HashSet<String>();
	
	private static Map<String, List<MessageTimeStampDTO>> topicToMsgMap = new HashMap<String, List<MessageTimeStampDTO>>();

	public static boolean isActive(String userId) {
		return activeUsers.contains(userId);
	}
	
	public static void deactivateUser(String userId) {
		if(activeUsers.contains(userId)) {
			Long ts = Long.valueOf(Instant.now().toEpochMilli());
			userToLastActiveMap.put(userId, ts);
			disconnectedUsers.add(userId);
			activeUsers.remove(userId);
		}
	}
	
	public static void activateUser(String userId) {
		if(!activeUsers.contains(userId)) {
			Long ts = Long.valueOf(Instant.now().toEpochMilli());
			userToLastActiveMap.put(userId, ts);
			activeUsers.add(userId);
			disconnectedUsers.remove(userId);
		}
	}
	
	public static List<MessageTimeStampDTO> getAllMessagesFromTS(String topic, long ts) {
		List<MessageTimeStampDTO> msgs = new ArrayList<MessageTimeStampDTO>();
		if(topicToMsgMap.containsKey(topic)) {
			msgs = topicToMsgMap.get(topic);
			msgs = msgs.stream().filter(k-> k.getTimestamp() > ts).collect(Collectors.toList());
		}
		return msgs;
	}
	
	public static void addMessagesToTopic(Type type, String topic, String content, String userId) {
		List<MessageTimeStampDTO> msgs = topicToMsgMap.getOrDefault(topic, new ArrayList<MessageTimeStampDTO>());
		msgs.add(new MessageTimeStampDTO(type, content, userId, Instant.now().toEpochMilli()));
		topicToMsgMap.put(topic, msgs);
	}
}
