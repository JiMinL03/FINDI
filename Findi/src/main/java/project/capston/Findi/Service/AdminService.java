package project.capston.Findi.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import project.capston.Findi.Controller.AdminController;
import project.capston.Findi.Entity.Member;
import project.capston.Findi.Repository.MemberRepository;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    public List<AdminController.MemberView> getAllMembersForAdmin() {
        List<Member> members = memberRepository.findAll();

        return members.stream().map(m -> {
            String base64Img = "";
            if (m.getImg() != null && m.getImg().length > 0) {
                base64Img = Base64.getEncoder().encodeToString(m.getImg());
            }
            return new AdminController.MemberView(m.getId(), m.getUsername(), m.getJob(), base64Img);
        }).collect(Collectors.toList());
    }

    public void accept(String id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원 없음"));
        member.setActive(true);
        memberRepository.save(member);
    }
    public void reject(String id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원 없음"));
        member.setActive(false);
        memberRepository.save(member);
    }
}
