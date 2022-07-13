package com.anshul.chat_log.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class ChatLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "user_unique_name", nullable = false)
	private String userUniqueName;
	
	@Column(nullable = false)
	private String message;
	
	@Column(name = "is_sent", nullable = false, columnDefinition="boolean")
	private boolean isSent;
	
	@Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt;

	public ChatLog(String userUniqueName, String message, boolean isSent, LocalDateTime createdAt) {
		this.userUniqueName = userUniqueName;
		this.message = message;
		this.isSent = isSent;
		this.createdAt = createdAt;
	}
	
}
