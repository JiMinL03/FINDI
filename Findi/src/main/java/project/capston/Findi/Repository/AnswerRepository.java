package project.capston.Findi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.capston.Findi.Entity.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

}
