package com.sajoproject.sajotuna.message.entity;

import com.sajoproject.sajotuna.message.dto.createMessageDto.CreateMessageDtoRequest;
import com.sajoproject.sajotuna.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "message")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")  // 컬럼 이름 명시
    private Long messageId;

    private String content;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User senderId;

    @CreatedDate
    @Column(name="created_at",updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    public Message(CreateMessageDtoRequest reqDto){
        this.content=reqDto.getContent();

        User newReceiverId = new User();
        newReceiverId.setUserId(reqDto.getReceiverId());
        receiverId=newReceiverId;

        User newSenderId = new User();
        newSenderId.setUserId(reqDto.getSenderId());
        senderId=newSenderId;


    }


}
