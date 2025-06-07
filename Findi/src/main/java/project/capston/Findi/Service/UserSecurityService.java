package project.capston.Findi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.capston.Findi.Entity.Member;
import project.capston.Findi.MemberRole;
import project.capston.Findi.Repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // Member의 ID가 String이므로, 변환 없이 바로 사용
        Optional<Member> optionalMember = this.memberRepository.findById(userId);

        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다. ID=" + userId);
        }

        Member member = optionalMember.get();
        System.out.println(">> 로그인 시도 ID: " + member.getId() + ", active=" + member.isActive());

        if (!member.isActive()) {
            throw new DisabledException("관리자 승인 대기 중이거나, 비활성화된 계정입니다.");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(member.getUsername())) {
            authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));
        }

        // Security의 User 객체는 username, password, 권한 리스트가 필요
        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}
