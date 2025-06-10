package project.capston.Findi.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.capston.Findi.Entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Page<Question> findAll(Pageable pageable);
    @Query("SELECT q FROM Question q WHERE q.subject LIKE %:kw% OR q.content LIKE %:kw%")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
