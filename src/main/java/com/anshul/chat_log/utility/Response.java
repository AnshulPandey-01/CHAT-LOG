package com.anshul.chat_log.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Response {
	private Status status;
	private Object response;
}
