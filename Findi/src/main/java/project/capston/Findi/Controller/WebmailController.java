package project.capston.Findi.Controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.capston.Findi.Entity.Member;
import project.capston.Findi.Entity.Roommate;
import project.capston.Findi.Service.MemberService;
import project.capston.Findi.Service.RoommateService;
import project.capston.Findi.Service.WebmailService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WebmailController {
    public final WebmailService webmailService;
    private final MemberService memberService;
    private final RoommateService roommateService;
    @GetMapping("/send-roommate")
    public String sendRoommateMail(@RequestParam String to) throws Exception {
        webmailService.sendRoommateMail(
                to,
                "홍길동", "20241234", "컴퓨터공학과", "남", "비흡연", "INTJ", "정돈된 생활", "test@example.com"
        );
        return "메일 전송 완료";
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, String> payload) throws Exception {
        System.out.println("payload: " + payload); // ★ 요청 데이터 확인
        try {
            String toName = payload.get("toName");
            System.out.println("toName: " + toName);

            Member member = memberService.getMember(toName);
            Roommate roommate = roommateService.findByName(member.getUsername());

            if (member == null) {
                System.err.println("No member found with name: " + toName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("status", "fail", "message", "해당 이름의 이메일을 찾을 수 없습니다."));
            }

            String toEmail = member.getEmail();
            System.out.println("toEmail: " + toEmail);

            webmailService.sendRoommateMail(
                    toEmail,
                    toName, roommate.getStudent_id(), roommate.getMajor(),
                    roommate.getGender() == 0 ? "남성" : "여성",
                    roommate.getIs_Smoking() == 0 ? "비흡연" : "흡연",
                    roommate.getMbti(),
                    roommate.getLife_pattern() == 0 ? "아침형" : "저녁형",
                    toEmail
            );

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", toName + "에게 이메일 발송 완료"
            ));
        } catch (MessagingException e) {
            e.printStackTrace(); // 예외 전체 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "fail", "message", "이메일 발송 실패"));
        }
    }


}
