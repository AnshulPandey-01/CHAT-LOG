package com.anshul.chat_log.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.anshul.chat_log.entity.ChatLog;

@Repository
public interface ChatLogRepo extends JpaRepository<ChatLog, Long> {

	List<ChatLog> findByUserUniqueName(String name);
	
	@Query(value = "select * from chat_log where user_unique_name = :user order by created_at desc limit :limit", nativeQuery = true)
	List<ChatLog> getLatestLogs(@Param("user") String user, @Param("limit") int limit);
	
	@Query(value = "select logs.* from (select * from chat_log where user_unique_name = :user and id >= :from order by id asc limit :limit) as logs order by created_at desc", nativeQuery = true)
	List<ChatLog> getLogsFrom(@Param("user") String user, @Param("from") int from, @Param("limit") int limit);
	
	int deleteByUserUniqueName(String name);
	
	Optional<ChatLog> findByIdAndUserUniqueName(long id, String name);
	
}
