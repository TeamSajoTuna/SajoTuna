package com.sajoproject.sajotuna.message.service;

import com.sajoproject.sajotuna.message.dto.createMessageDto.CreateMessageDtoRequest;
import com.sajoproject.sajotuna.message.dto.createMessageDto.CreateMessageDtoResponse;
import com.sajoproject.sajotuna.message.entity.Message;
import com.sajoproject.sajotuna.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public CreateMessageDtoResponse createMessage(CreateMessageDtoRequest reqDto){
        Message message = new Message(reqDto);
        Message saveMessage = messageRepository.save(message);
        return new CreateMessageDtoResponse(saveMessage);

    }

}
