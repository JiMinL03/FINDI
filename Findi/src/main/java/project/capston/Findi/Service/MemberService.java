package project.capston.Findi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.capston.Findi.Entity.Member;
import project.capston.Findi.Repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    Member member = new Member();
    public void create(String id, String password, String username, String job, byte[] img){
        //회원정보 저장
        member.setId(id);
        member.setPassword(passwordEncoder.encode(password));
        member.setUsername(username);
        member.setJob(job);
        member.setImg(img);
        memberRepository.save(member);
    }
    public boolean existsId(String id){
        Optional<Member> member = memberRepository.findById(id);
        return member.isPresent();
    }

    public boolean existsUsername(String username){
        Optional<Member> member = memberRepository.findByUsername(username);
        return member.isPresent();
    }

    public Member getMember(String username){
        Optional<Member> member = memberRepository.findByUsername(username);
        if(member.isPresent()){
            return member.get();
        }else{
            throw new UsernameNotFoundException("Username not found");
        }
    }
}
