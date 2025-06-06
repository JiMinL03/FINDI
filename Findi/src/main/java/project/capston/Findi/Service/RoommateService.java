package project.capston.Findi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.capston.Findi.Entity.Roommate;
import project.capston.Findi.Repository.RoommateRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoommateService {
    private final RoommateRepository roommateRepository;

    public void create(int gender, String name, String birth, String student_id, String mbti, String major, int is_Smoking, int life_pattern) {
        Roommate roommate = new Roommate();

        roommate.setGender(gender);
        roommate.setName(name);
        roommate.setBirth(birth);
        roommate.setStudent_id(student_id);
        roommate.setMbti(mbti);
        roommate.setMajor(major);
        roommate.setIs_Smoking(is_Smoking);
        roommate.setLife_pattern(life_pattern);
        roommateRepository.save(roommate);
    }

    public Roommate saveRoommate(Roommate roommate) {
        System.out.println("Roommate 저장 시도: " + roommate.getName());
        return roommateRepository.save(roommate); // 저장 후 return
    }

    public List<Roommate> getAllRoommates() {
        return roommateRepository.findAll();
    }
}

