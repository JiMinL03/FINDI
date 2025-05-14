package project.capston.Findi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.capston.Findi.Entity.MemberProfile;
import project.capston.Findi.Repository.MemberProfileRepository;

@Service
@RequiredArgsConstructor
public class MemberProfileService {
    private final MemberProfileRepository memberProfileRepository;
    MemberProfile member = new MemberProfile();
    public void create(String id,String name ,String student_id, int gender, String major, String mbti, int isSmoking, int life_pattern, String birth){
        member.setId(id);
        member.setName(name);
        member.setStudent_id(student_id);
        member.setGender(gender);
        member.setMajor(major);
        member.setMbti(mbti);
        member.setIsSmoking(isSmoking);
        member.setLife_pattern(life_pattern);
        member.setBirth(birth);
        memberProfileRepository.save(member);
    }
}
