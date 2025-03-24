package project.capston.Findi.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.capston.Findi.Entity.Member;
import project.capston.Findi.Form.MemberForm;
import project.capston.Findi.Service.MemberService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MemberController {
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
    public String register(@Valid MemberForm memberForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) { //폼 유효성 검사
            return "redirect:/member/register?error=validation_failed";
        }

        if(!memberForm.getPassword().equals(memberForm.getPassword2())){ //비밀번호 재확인 일치 여부 
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
            return "redirect:/member/register?error=password_mismatch";
        }

        if(memberService.existsId(memberForm.getId())){ //아이디 개체무결성 확인 여부
            return "redirect:/member/register?error=id_exists";
        }

        if(memberService.existsUsername(memberForm.getUsername())){ //아이디 개체무결성 확인 여부
            return "redirect:/member/register?error=username_exists";
        }

        memberService.create(memberForm.getId(), memberForm.getPassword(), memberForm.getUsername(), memberForm.getJob(), memberForm.getImg());
        //위 오류에 해당사항 없을 시, 회원 정보 저장
        System.out.println("새로운 회원 생성됨: " + memberForm.getUsername());
        return "main";
    }

    @GetMapping("/member/login")
    public String signin() {
        return "login";
    }
}
