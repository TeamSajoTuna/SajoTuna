package com.sajoproject.sajotuna.message.repository;

import com.sajoproject.sajotuna.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {
}
