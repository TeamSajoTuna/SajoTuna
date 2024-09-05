package com.sajoproject.sajotuna.message.dto.getRecievedBoxDto;

import com.sajoproject.sajotuna.message.entity.Message;
import com.sajoproject.sajotuna.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetReceivedBoxDtoResponse {
    private Long messageId;
    private String content;
    private Long receiverId;
    private Long senderId;
    private LocalDateTime createdAt;

    public GetReceivedBoxDtoResponse(Message message){
        this.messageId=message.getMessageId();
        this.content=message.getContent();
        this.receiverId=message.getReceiverId().getUserId();
        this.senderId=message.getSenderId().getUserId();
        this.createdAt=message.getCreatedAt();

    }

}
