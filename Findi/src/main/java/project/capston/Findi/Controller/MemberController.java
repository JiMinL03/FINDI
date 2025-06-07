package project.capston.Findi.Controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import project.capston.Findi.Entity.Member;
import project.capston.Findi.Form.MemberForm;
import project.capston.Findi.Service.MemberService;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signup() {
        return "term";
    }

    @GetMapping("/member/register")
    public String register(Model model) {
        model.addAttribute("registerForm", new MemberForm());
        return "register";
    }

    @PostMapping("/member/register")
    public String register(@Valid MemberForm memberForm, BindingResult bindingResult) throws IOException {
        if(bindingResult.hasErrors()) {
            return "redirect:/member/register?error=validation_failed";
        }

        if(!memberForm.getPassword().equals(memberForm.getPassword2())){
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
            return "redirect:/member/register?error=password_mismatch";
        }

        if(memberService.existsId(memberForm.getId())){
            return "redirect:/member/register?error=id_exists";
        }

        if(memberService.existsUsername(memberForm.getUsername())){
            return "redirect:/member/register?error=username_exists";
        }
        byte[] imgBytes = memberForm.getImg().getBytes();
        memberService.create(memberForm.getId(), memberForm.getPassword(), memberForm.getUsername(), memberForm.getJob(), imgBytes, memberForm.getEmail());
        System.out.println("새로운 회원 생성됨: " + memberForm.getUsername());
        return "redirect:/";
    }

    @GetMapping("/member/login")
    public String signin() {
        return "login";
    }

    @PostMapping("/member/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        Member member = memberService.getMember(username);

        if (member != null && passwordEncoder.matches(password, member.getPassword())) {
            session.setAttribute("member", member); // ✅ 세션에 저장
            return "redirect:/"; // 또는 홈으로 리디렉션
        } else {
            model.addAttribute("loginError", true);
            return "login";
        }
    }
}
