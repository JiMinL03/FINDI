package project.capston.Findi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.capston.Findi.Entity.ChatRoom;
import project.capston.Findi.Repository.ChatRoomRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    public void saveAll() {
        String[] names = {
                "동의지천융합대학",
                "인문사회과학대학",
                "상경대학",
                "미래융합대학",
                "의료·보건·생활대학",
                "한의과대학",
                "공과대학",
                "소프트웨어융합대학",
                "예술디자인체육대학",
                "자유전공"
        };

        for (String name : names) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setName(name);
            chatRoomRepository.save(chatRoom);
        }
    }

    public List<ChatRoom> getAllChatRoom() {
        chatRoomRepository.findAll();
        return chatRoomRepository.findAll();
    }

    public ChatRoom getChatRoomByName(String name) {
        chatRoomRepository.findByName(name);
        return chatRoomRepository.findByName(name);
    }

    public Optional<ChatRoom> getChatRoomById(Long id) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(id);
        return chatRoom;
    }
}
