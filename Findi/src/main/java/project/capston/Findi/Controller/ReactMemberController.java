package project.capston.Findi.Controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.capston.Findi.Entity.Member;
import project.capston.Findi.Service.MemberService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class ReactMemberController {
    private final MemberService memberService;
    @GetMapping("/session-info")
    @ResponseBody
    public Map<String, Object> getSessionInfo(HttpSession session, Principal principal) {
        Map<String, Object> response = new HashMap<>();

        String username = principal.getName();
        Member member = memberService.getMember(username);

        if (member != null) {
            log.info("세션에서 사용자 정보 확인: username={}, email={}", member.getUsername(), member.getEmail());
            response.put("username", member.getUsername());
            response.put("email", member.getEmail());
        } else {
            log.warn("세션에 로그인된 사용자 정보 없음");
            response.put("error", "로그인되어 있지 않음");
        }

        return response;
    }
}
