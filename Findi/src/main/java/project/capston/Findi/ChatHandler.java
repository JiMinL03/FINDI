package project.capston.Findi;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatHandler extends TextWebSocketHandler {
    private int userCount = 0;
    // 현재 접속 중인 세션 목록 (중복 허용 안 함)
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final Map<WebSocketSession, String> sessionNicknames = new ConcurrentHashMap<>();

    private void broadcast(String message) throws Exception {
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(message));
            }
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception { //클라이언트로부터 텍스트 메시지를 수신했을 때 호출
        String sender = sessionNicknames.get(session); // 고정 닉네임
        String payload = message.getPayload();
        String json = String.format("{\"sender\":\"%s\", \"message\":\"%s\", \"userCount\":%d}", sender, payload, userCount);

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(json));
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        userCount++;
        String nickname = "User" + userCount;
        sessionNicknames.put(session, nickname);
        session.sendMessage(new TextMessage("Your nickname: " + nickname));
        broadcast("현재 접속자 수: " + sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        userCount--;
        System.out.println("접속 종료: " + session.getId());
        System.out.println("현재 접속자 수: " + sessions.size());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("오류 발생: " + exception.getMessage());
    }
}
