package chatserver.chat.controller;

import chatserver.chat.dto.ChatMessageReqDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {

    private final SimpMessageSendingOperations messagingTemplate;

    public StompController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 방법1. MessageMapping(수신)과 SenTo(topic에 메시지전달) 한꺼번에 처리
    // DestinationVariable : @MessageMapping 으로 정의된 Websocket Controller 내에서만 사용
    /*@MessageMapping("/{roomId}") // 클라이언트에서 특정 publish/roomId형태로 메시지를 발생 시 MessageMapping 수신
    @SendTo("/topic/{roomId}") // 해당 roomId에 메시지를 발행하여 구독중인 클라이언트에게 메시지를 전송
    public String sendMessage(@DestinationVariable Long roomId, String message) {
        System.out.println(message);
        return message;
    }*/

    // 방법2. MessageMapping 애노테이션만 활용
    @MessageMapping("/{roomId}") // 클라이언트에서 특정 publish/roomId형태로 메시지를 발생 시 MessageMapping 수신
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageReqDto chatMessageReqDto) {
        System.out.println(chatMessageReqDto.getMessage());
        messagingTemplate.convertAndSend("/topic/" + roomId, chatMessageReqDto);
    }
}
