package com.github.kaklakariada.fritzbox.report.model;

import java.io.Serializable;
import java.time.Instant;

public class EmailMetadata implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String messageId;
	private final Instant timestamp;
	private final String subject;

	public EmailMetadata(String messageId, Instant timestamp, String subject) {
		this.messageId = messageId;
		this.timestamp = timestamp;
		this.subject = subject;
	}

	public String getMessageId() {
		return messageId;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public String getSubject() {
		return subject;
	}

	@Override
	public String toString() {
		return "EmailMetadata [" +
				"timestamp=" + timestamp
				+ ", messageId=" + messageId
				+ ", subject=" + subject + "]";
	}
}
