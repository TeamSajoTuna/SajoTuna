package com.sajoproject.sajotuna.message.contoller;

import com.sajoproject.sajotuna.annotation.Auth;
import com.sajoproject.sajotuna.following.dto.followDto.FollowDtoRequest;
import com.sajoproject.sajotuna.following.dto.followDto.FollowDtoResponse;
import com.sajoproject.sajotuna.message.dto.createMessageDto.CreateMessageDtoRequest;
import com.sajoproject.sajotuna.message.dto.createMessageDto.CreateMessageDtoResponse;
import com.sajoproject.sajotuna.message.service.MessageService;
import com.sajoproject.sajotuna.user.dto.authUserDto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<CreateMessageDtoResponse> createMessage (@RequestBody CreateMessageDtoRequest reqDto, @Auth AuthUser authUser) {
        reqDto.setSenderId(authUser.getId());
        CreateMessageDtoResponse resDto = messageService.createMessage(reqDto);
        return ResponseEntity.ok().body(resDto);
    }

}
