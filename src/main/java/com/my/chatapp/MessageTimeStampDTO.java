package com.my.chatapp;

public class MessageTimeStampDTO extends MessageDTO{

	private long timestamp;

	public MessageTimeStampDTO(Type type, String content, String userId, long timestamp) {
		super(type, content, userId);
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "MessageTimeStampDTO [timestamp=" + timestamp + ", getType()=" + getType() + ", getContent()="
				+ getContent() + ", getUserId()=" + getUserId() + "]";
	}

}
