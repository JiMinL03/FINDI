package project.capston.Findi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.capston.Findi.Entity.Answer;
import project.capston.Findi.Entity.Member;
import project.capston.Findi.Entity.Question;
import project.capston.Findi.Repository.AnswerRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    public void create(String content, Question question, Member author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setQuestion(question);
        answer.setAuthor(author);
        answer.setCreateDate(LocalDate.now());
        this.answerRepository.save(answer);
    }

    public void delete(Integer id) {
        answerRepository.deleteById(id);
    }

    public Answer getAnswer(Integer id) {
        Answer answer = this.answerRepository.findById(id).get();
        return answer;
    }
}
