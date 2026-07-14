package chatserver.chat.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// connect 웹소켓 요청이 들어왔을때, 이를 처리할 클래스
@Component
public class SimpleWebSocketHandler extends TextWebSocketHandler  {
    // 연결된 세션 관리 : 스레드 safe한 set 사용
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        // slf4j 로깅 으로 변경하기 todo
        System.out.println("Connected : " + session.getId());
    }

    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message : " + payload);

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(payload));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Disconnected : " + session.getId());
    }

}
