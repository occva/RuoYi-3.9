package com.ruoyi.user.repository;

import com.ruoyi.user.domain.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * AI聊天消息 MongoDB Repository
 */
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    /**
     * 按会话ID查询所有消息（按时间正序）
     */
    List<ChatMessage> findBySessionIdOrderByCreateTimeAsc(String sessionId);

    List<ChatMessage> findBySessionIdAndUserIdOrderByCreateTimeAsc(String sessionId, Long userId);

    List<ChatMessage> findBySessionIdAndGuestIdAndUserIdIsNullOrderByCreateTimeAsc(String sessionId, String guestId);

    /**
     * 按会话ID查询最近N条消息（按时间倒序，用于构建上下文）
     */
    List<ChatMessage> findTop20BySessionIdAndUserIdOrderByCreateTimeDesc(String sessionId, Long userId);

    List<ChatMessage> findTop20BySessionIdAndGuestIdAndUserIdIsNullOrderByCreateTimeDesc(String sessionId, String guestId);

    List<ChatMessage> findTop20BySessionIdAndUserIdIsNullOrderByCreateTimeDesc(String sessionId);

    /**
     * 按用户ID查询消息
     */
    List<ChatMessage> findByUserIdOrderByCreateTimeDesc(Long userId);

    List<ChatMessage> findByGuestIdAndUserIdIsNullOrderByCreateTimeDesc(String guestId);

    Optional<ChatMessage> findFirstByUserIdOrderByCreateTimeDesc(Long userId);

    long countByGuestIdAndRole(String guestId, String role);

    long countBySessionIdAndUserIdIsNullAndRole(String sessionId, String role);

    void deleteBySessionIdAndUserId(String sessionId, Long userId);

    void deleteBySessionIdAndGuestIdAndUserIdIsNull(String sessionId, String guestId);
}
