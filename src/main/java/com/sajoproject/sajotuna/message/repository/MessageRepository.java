package com.sajoproject.sajotuna.message.repository;

import com.sajoproject.sajotuna.message.entity.Message;
import com.sajoproject.sajotuna.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    Message findByMessageIdAndReceiverIdAndIsDeleted( Long  messageId, User receiverId, boolean trueValue);

    List<Message> findByReceiverId_UserIdAndIsDeleted(Long id, boolean f);

    List<Message> findBySenderId_UserId(Long id);
}
