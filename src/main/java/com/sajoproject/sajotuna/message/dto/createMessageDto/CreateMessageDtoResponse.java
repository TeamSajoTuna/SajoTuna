package com.sajoproject.sajotuna.message.dto.createMessageDto;

import com.sajoproject.sajotuna.message.entity.Message;
import com.sajoproject.sajotuna.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CreateMessageDtoResponse {
    private Long messageId;

    private String content;

    private Long receiverId;

    private Long senderId;

    private LocalDateTime createdAt;

    public CreateMessageDtoResponse(Message message){
        this.messageId=message.getMessageId();
        this.content=message.getContent();
        this.receiverId=message.getReceiverId().getUserId();
        this.senderId=message.getSenderId().getUserId();
        this.createdAt=message.getCreatedAt();

    }

}
