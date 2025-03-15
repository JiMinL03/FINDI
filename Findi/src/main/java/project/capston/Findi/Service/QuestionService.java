package project.capston.Findi.Service;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import project.capston.Findi.Entity.Answer;
import project.capston.Findi.Entity.Member;
import project.capston.Findi.Entity.Question;
import project.capston.Findi.Repository.QuestionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    public void create(String subject, String content, byte[] img, Member author){
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setImg(img);
        q.setCreateDate(LocalDate.now());
        q.setAuthor(author);
        questionRepository.save(q);
    }

    public Question getQuestion(Integer id){
        Optional<Question> question = questionRepository.findById(id);
        if(question.isPresent()){
            return question.get();
        }else{
            throw new NullPointerException("Question not found");
        }
    }

    public List<Question> getList(){
        return questionRepository.findAll();
    }

    public void deleteQuestion(Integer id){
        questionRepository.deleteById(id);
    }

    public void updateQuestion(Question question, String Subject, String Content, byte[] img){
        question.setSubject(Subject);
        question.setContent(Content);
        question.setImg(img);
        questionRepository.save(question);
    }

    public Page<Question> getPage(int page){
        Pageable pageable = PageRequest.of(page,10);
        return this.questionRepository.findAll(pageable);
    }
}
