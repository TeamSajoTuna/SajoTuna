package com.sajoproject.sajotuna.message.contoller;
import com.sajoproject.sajotuna.annotation.Auth;
import com.sajoproject.sajotuna.message.dto.createMessageDto.CreateMessageDtoRequest;
import com.sajoproject.sajotuna.message.dto.createMessageDto.CreateMessageDtoResponse;
import com.sajoproject.sajotuna.message.dto.deleteMessageDto.DeleteMessageDtoRequest;
import com.sajoproject.sajotuna.message.dto.deleteMessageDto.DeleteMessageDtoResponse;
import com.sajoproject.sajotuna.message.dto.getRecievedBoxDto.GetReceivedBoxDtoResponse;
import com.sajoproject.sajotuna.message.dto.getSendBoxDto.GetSendBoxDtoResponse;
import com.sajoproject.sajotuna.message.service.MessageService;
import com.sajoproject.sajotuna.user.dto.authUserDto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
    쪽지 보내기
     @param reqDto : "content":"내용", "receiverId":받는 사람 id
     @param authUser : authUser.getId() = 보내는 사람 id
     @return 보낸 쪽지 내용
    */
    @PostMapping
    public ResponseEntity<CreateMessageDtoResponse> createMessage (@RequestBody CreateMessageDtoRequest reqDto, @Auth AuthUser authUser) {
        reqDto.setSenderId(authUser.getId());
        CreateMessageDtoResponse resDto = messageService.createMessage(reqDto);

        return ResponseEntity.ok().body(resDto);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMessage( @Auth AuthUser authUser, @RequestBody DeleteMessageDtoRequest reqDto){
        messageService.deleteMessage(authUser,reqDto);
        return ResponseEntity.ok().body("쪽지 삭제 완료");
    }

    @GetMapping("/received-box")
    public ResponseEntity<List<GetReceivedBoxDtoResponse>> getReceivedBox(@Auth AuthUser authUser){
        List<GetReceivedBoxDtoResponse> resDto = messageService.getReceivedBox(authUser.getId());
        return ResponseEntity.ok().body(resDto);
    }

    @GetMapping("/send-box")
    public ResponseEntity<List<GetSendBoxDtoResponse>> getSendBox(@Auth AuthUser authUser){
        List<GetSendBoxDtoResponse> resDto = messageService.getSendBox(authUser.getId());
        return ResponseEntity.ok().body(resDto);
    }


}
