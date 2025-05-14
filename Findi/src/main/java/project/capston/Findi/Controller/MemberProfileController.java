package project.capston.Findi.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import project.capston.Findi.Entity.Member;
import project.capston.Findi.Form.MemberForm;
import project.capston.Findi.Form.MemberProfileForm;
import project.capston.Findi.Repository.MemberRepository;
import project.capston.Findi.Service.MemberProfileService;
import project.capston.Findi.Service.MemberService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MemberProfileController {
    private final MemberProfileService memberProfileService;
    private final MemberService memberService;
    @PostMapping("/mattingForm")
    public String mattingForm(@Valid MemberProfileForm memberProfileForm,
                              BindingResult bindingResult,
                              Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formErrors", bindingResult.getAllErrors());
            return "mattingForm";
        }
        String username = principal.getName();
        Member member = memberService.getMember(username);
        String memberId = member.getId();
        memberProfileService.create(memberId, memberProfileForm.getName(), memberProfileForm.getStudent_id(), memberProfileForm.getGender(), memberProfileForm.getMajor(), memberProfileForm.getMbti(), memberProfileForm.getIsSmoking(),memberProfileForm.getLife_pattern(), memberProfileForm.getBirth());
        return "redirect:/"; // 성공 후 보여줄 페이지 이름
    }
}
