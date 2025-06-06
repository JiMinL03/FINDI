package project.capston.Findi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.capston.Findi.Entity.ChatRoom;
import project.capston.Findi.Service.ChatRoomService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatroomController {
    private final ChatRoomService chatRoomService;
    @GetMapping("/chat/room/0")
    public String chat(Model model) {
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRoom();
        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("roomId", 0);
        model.addAttribute("chatRoom", chatRooms.get(0).getName());
        return "chat";
    }

    @GetMapping("/chat/room/{id}")
    public String chatRoom(@PathVariable Long id, Model model) {
        Optional<ChatRoom> room = chatRoomService.getChatRoomById(id);
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRoom();
        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("roomId", id);
        model.addAttribute("chatRoom", room.get().getName());
        return "chat"; // 새 템플릿 파일로 분리하거나 기존 chat.html 재사용도 가능
    }

    @GetMapping("/chat/fetch")
    public ResponseEntity<String> saveChatroom() throws IOException {
        chatRoomService.saveAll();
        return ResponseEntity.ok("채팅방 그룹 데이터를 성공적으로 저장했습니다.");
    }

}
