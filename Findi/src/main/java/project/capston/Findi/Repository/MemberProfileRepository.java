package project.capston.Findi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.capston.Findi.Entity.MemberProfile;

@Repository
public interface MemberProfileRepository extends JpaRepository<MemberProfile, String> {
}
