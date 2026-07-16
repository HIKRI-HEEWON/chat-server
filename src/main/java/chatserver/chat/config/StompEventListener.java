package chatserver.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//스프링과 stomp는 기본적으로 세션관리를 자동(내부적)으로 처리해줌
//연결/해제 이벤트를 기록, 연결된 세션 수를 실시간으로 확인할 목적으로 이벤트 리스너를 생성 => 로그, 디버깅 목적
@Component
@Slf4j
public class StompEventListener {

    private final Set<String> sessions = ConcurrentHashMap.newKeySet();

    // connection 이 발생할 떄 해당 이벤트 리스너가 호출됨
    // StompSessionConnectedEvent
    @EventListener
    public void connectHandle(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessions.add(accessor.getSessionId());
        log.info("== connect session id : {}", accessor.getSessionId());
        log.info("== connect session total : {}", sessions.size());
    }

    @EventListener
    public void disConnectHandle(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessions.remove(accessor.getSessionId());
        log.info("== DisConn session id : {}", accessor.getSessionId());
        log.info("== connect session total : {}", sessions.size());
    }

}
