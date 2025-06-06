package project.capston.Findi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.capston.Findi.Entity.Roommate;

public interface RoommateRepository extends JpaRepository<Roommate, Long> {
    Roommate findByName(String name);
}
