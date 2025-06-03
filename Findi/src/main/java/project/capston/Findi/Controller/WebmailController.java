package project.capston.Findi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.capston.Findi.Service.WebmailService;

@RestController
@RequiredArgsConstructor
public class WebmailController {
    public final WebmailService webmailService;
    @GetMapping("/send-roommate")
    public String sendRoommateMail(@RequestParam String to) throws Exception {
        webmailService.sendRoommateMail(
                to,
                "홍길동", "20241234", "컴퓨터공학과", "남", "비흡연", "INTJ", "정돈된 생활", "test@example.com"
        );
        return "메일 전송 완료";
    }
}
