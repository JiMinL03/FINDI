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
public class ChatWebSocketHandler extends TextWebSocketHandler {
    // 채팅방별 세션 목록
    private final Map<Long, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    // 닉네임 저장
    private final Map<WebSocketSession, String> sessionNicknames = new ConcurrentHashMap<>();

    // 채팅방별 유저 수
    private final Map<Long, Integer> roomUserCounts = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long roomId = extractRoomId(session);
        roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);

        int userCount = roomUserCounts.getOrDefault(roomId, 0) + 1;
        roomUserCounts.put(roomId, userCount);

        String nickname = "User" + userCount;
        sessionNicknames.put(session, nickname);

        // 닉네임 전달
        session.sendMessage(new TextMessage("Your nickname: " + nickname));

        // 입장 알림
        broadcast(roomId, String.format("🔔 %s 님이 입장하셨습니다. 현재 인원: %d명", nickname, userCount));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long roomId = extractRoomId(session);
        String sender = sessionNicknames.get(session);
        String payload = message.getPayload();
        int userCount = roomUserCounts.getOrDefault(roomId, 0);
        String json = String.format("{\"sender\":\"%s\", \"message\":\"%s\", \"userCount\":%d}", sender, payload, userCount);
        for (WebSocketSession s : roomSessions.getOrDefault(roomId, Set.of())) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(json));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long roomId = extractRoomId(session);
        Set<WebSocketSession> sessions = roomSessions.getOrDefault(roomId, Set.of());
        sessions.remove(session);

        int newCount = Math.max(0, roomUserCounts.getOrDefault(roomId, 1) - 1);
        roomUserCounts.put(roomId, newCount);

        String nickname = sessionNicknames.getOrDefault(session, "Unknown");
        broadcast(roomId, String.format("❌ %s 님이 퇴장하셨습니다. 현재 인원: %d명", nickname, newCount));

        sessionNicknames.remove(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("WebSocket 오류: " + exception.getMessage());
    }

    private void broadcast(Long roomId, String message) throws Exception {
        for (WebSocketSession s : roomSessions.getOrDefault(roomId, Set.of())) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(message));
            }
        }
    }

    private Long extractRoomId(WebSocketSession session) {
        String uri = session.getUri().toString(); // 예: /ws/chat/room/3
        String[] parts = uri.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }
}
