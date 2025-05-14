package project.capston.Findi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.capston.Findi.Repository.MemberProfileRepository;

@Service
@RequiredArgsConstructor
public class MemberProfileService {
    private final MemberProfileRepository memberProfileRepository;
}
