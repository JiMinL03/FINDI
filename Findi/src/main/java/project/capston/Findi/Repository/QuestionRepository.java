package project.capston.Findi.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.capston.Findi.Entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Page<Question> findAll(Pageable pageable);
}
