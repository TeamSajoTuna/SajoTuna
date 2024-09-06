package com.sajoproject.sajotuna.message.dto.createMessageDto;

import com.sajoproject.sajotuna.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
public class CreateMessageDtoRequest {
    private String content;
    private Long senderId;
    private Long receiverId;

}
