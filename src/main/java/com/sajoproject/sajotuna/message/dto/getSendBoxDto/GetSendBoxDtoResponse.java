package com.sajoproject.sajotuna.message.dto.getSendBoxDto;

import com.sajoproject.sajotuna.message.entity.Message;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class GetSendBoxDtoResponse {
    private Long messageId;
    private String content;
    private Long receiverId;
    private Long senderId;
    private LocalDateTime createdAt;

    public GetSendBoxDtoResponse(Message message){
        this.messageId=message.getMessageId();
        this.content=message.getContent();
        this.receiverId=message.getReceiverId().getUserId();
        this.senderId=message.getSenderId().getUserId();
        this.createdAt=message.getCreatedAt();

    }

}
