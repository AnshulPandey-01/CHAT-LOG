package com.anshul.chat_log.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anshul.chat_log.entity.ChatLog;
import com.anshul.chat_log.repos.ChatLogRepo;
import com.anshul.chat_log.utility.CreateLogRequest;
import com.anshul.chat_log.utility.PageModel;
import com.anshul.chat_log.utility.Response;
import com.anshul.chat_log.utility.Status;

@RestController
@RequestMapping("chatlogs")
public class ChatLogController {

	private static final int MIN_USER_NAME_LENGTH = 4;
	private static final int MAX_USER_NAME_LENGTH = 16;
	private static final String USER_LENGTH_ERROR_MESSAGE = "user length is less than " + MIN_USER_NAME_LENGTH + " OR more than " + MAX_USER_NAME_LENGTH + " characters.";
	
	@Autowired
	private ChatLogRepo chatLogRepo;
	
	private boolean checkUserLength(String user) {
		return StringUtils.isBlank(user) || user.length() < MIN_USER_NAME_LENGTH || user.length() > MAX_USER_NAME_LENGTH;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{user}")
	public ResponseEntity<Response> createChatLog(@PathVariable("user") String user, @RequestBody CreateLogRequest log) {
		if (checkUserLength(user)) {
			return new ResponseEntity<>(new Response(Status.ERROR, USER_LENGTH_ERROR_MESSAGE), HttpStatus.NOT_ACCEPTABLE);
		}
		
		if (StringUtils.isBlank(log.getText())) {
			return new ResponseEntity<>(new Response(Status.ERROR, "text cannot be null or empty."), HttpStatus.NOT_ACCEPTABLE);
		}
		
		if (Objects.isNull(log.getIsSent())) {
			return new ResponseEntity<>(new Response(Status.ERROR, "isSent cannot be null or empty."), HttpStatus.NOT_ACCEPTABLE);
		}
		
		if (StringUtils.isBlank(log.getCreatedAt())) {
			return new ResponseEntity<>(new Response(Status.ERROR, "createdAt cannot be null or empty."), HttpStatus.NOT_ACCEPTABLE);
		}
		
		LocalDateTime createdAt;
		try {
			createdAt = LocalDateTime.parse(log.getCreatedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		} catch (DateTimeParseException e) {
			return new ResponseEntity<>(new Response(Status.ERROR, "Invalid createdAt format."), HttpStatus.NOT_ACCEPTABLE);
		}
		
		ChatLog chatLog = new ChatLog(user, log.getText(), log.getIsSent(), createdAt);
		ChatLog savedLog = chatLogRepo.save(chatLog);
		
		Map<String, Long> response = Map.of("messageId", savedLog.getId());
		return new ResponseEntity<>(new Response(Status.SUCCESS, response), HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{user}")
	public ResponseEntity<Response> getChatLog(@PathVariable("user") String user, @RequestParam(required = false) Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer count) {
		if (checkUserLength(user)) {
			return new ResponseEntity<>(new Response(Status.ERROR, USER_LENGTH_ERROR_MESSAGE), HttpStatus.NOT_ACCEPTABLE);
		}
		
		List<ChatLog> logs = Objects.isNull(from) ? chatLogRepo.getLatestLogs(user, count) : chatLogRepo.getLogsFrom(user, from, count);
		if (CollectionUtils.isEmpty(logs)) {
			return new ResponseEntity<>(new Response(Status.ERROR, "No chat found for the user " + user), HttpStatus.NOT_FOUND);
		}
		
		long fromId = logs.stream().min(Comparator.comparingLong(ChatLog::getId)).get().getId();
		long toId = logs.stream().max(Comparator.comparingLong(ChatLog::getId)).get().getId();
		PageModel<ChatLog> page = new PageModel<ChatLog>(logs, fromId, toId, count, logs.size());
		
		return new ResponseEntity<>(new Response(Status.SUCCESS, page), HttpStatus.OK);
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.DELETE, value = "/{user}")
	public ResponseEntity<Response> deleteUserChat(@PathVariable("user") String user) {
		if (checkUserLength(user)) {
			return new ResponseEntity<>(new Response(Status.ERROR, USER_LENGTH_ERROR_MESSAGE), HttpStatus.NOT_ACCEPTABLE);
		}
		
		int rows = chatLogRepo.deleteByUserUniqueName(user);
		if (rows == 0) {
			return new ResponseEntity<>(new Response(Status.ERROR, "No chat found for user " + user), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new Response(Status.SUCCESS, "All chats deleted successfully"), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{user}/{msgid}")
	public ResponseEntity<Response> deleteUserChat(@PathVariable("user") String user, @PathVariable("msgid") long id) {
		if (checkUserLength(user)) {
			return new ResponseEntity<>(new Response(Status.ERROR, USER_LENGTH_ERROR_MESSAGE), HttpStatus.NOT_ACCEPTABLE);
		}
		
		ChatLog log = chatLogRepo.findByIdAndUserUniqueName(id, user).orElse(null);
		if (Objects.isNull(log)) {
			return new ResponseEntity<>(new Response(Status.ERROR, "Chat with given user and id not found."), HttpStatus.NOT_FOUND);
		}
		
		chatLogRepo.delete(log);
		return new ResponseEntity<>(new Response(Status.SUCCESS, "Chat deleted successfully"), HttpStatus.OK);
	}
	
}
