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

    public Roommate save(Roommate roommate) {
        return roommateRepository.save(roommate);
    }
    public Roommate findByName(String name) {
        return roommateRepository.findByName(name);
    }
    public List<Roommate> findAll() { return roommateRepository.findAll(); }
    public void delete(Roommate roommate) {
        roommateRepository.delete(roommate);
    }

}
