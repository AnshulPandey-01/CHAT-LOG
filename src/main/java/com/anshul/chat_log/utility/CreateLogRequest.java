package com.anshul.chat_log.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateLogRequest {
	
	private String message;

	private Boolean isSent;
	
    private String createdAt;
    
}
