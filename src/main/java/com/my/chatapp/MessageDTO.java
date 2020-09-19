package com.my.chatapp;

public class MessageDTO {

	private String content;
	
	private String userId;
	
	private Type type;
	
	public static enum Type{
		READ, SENT, DELIVER
	}
	
	public MessageDTO(Type type, String content, String userId) {
		super();
		this.type = type;
		this.content = content;
		this.userId = userId;
	}

	public Type getType() {
		return type;
	}

	public String getContent() {
		return content;
	}

	public String getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return "MessageDTO [content=" + content + ", userId=" + userId + "]";
	}

}
