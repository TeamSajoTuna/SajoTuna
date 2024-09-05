package com.sajoproject.sajotuna.message.service;

import com.sajoproject.sajotuna.exception.BadRequestException;
import com.sajoproject.sajotuna.message.dto.createMessageDto.CreateMessageDtoRequest;
import com.sajoproject.sajotuna.message.dto.createMessageDto.CreateMessageDtoResponse;
import com.sajoproject.sajotuna.message.dto.deleteMessageDto.DeleteMessageDtoRequest;
import com.sajoproject.sajotuna.message.entity.Message;
import com.sajoproject.sajotuna.message.repository.MessageRepository;
import com.sajoproject.sajotuna.user.dto.authUserDto.AuthUser;
import com.sajoproject.sajotuna.user.entity.User;
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

    public void deleteMessage(AuthUser authUser, DeleteMessageDtoRequest reqDto){
        User user = new User();
        user.setUserId(authUser.getId());
        Message deleteMessage = messageRepository.findByMessageIdAndReceiverIdAndIsDeleted(reqDto.getMessageId(),user,false);
        if(deleteMessage==null){
            throw new BadRequestException("존재하지 않는 쪽지입니다.");
        }
        deleteMessage.setIsDeleted(true);
        messageRepository.save(deleteMessage);


    }

}
