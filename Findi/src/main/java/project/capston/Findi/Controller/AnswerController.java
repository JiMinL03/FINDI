package project.capston.Findi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.capston.Findi.Entity.Answer;
import project.capston.Findi.Entity.Member;
import project.capston.Findi.Entity.Question;
import project.capston.Findi.Service.AnswerService;
import project.capston.Findi.Service.MemberService;
import project.capston.Findi.Service.QuestionService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final MemberService memberService;
    @PostMapping("/answer/create/{id}")
    public String create(@RequestParam("content") String content, @PathVariable int id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        Member author = this.memberService.getMember(principal.getName());
        answerService.create(content, question, author);
        return String.format("redirect:/question/detail/%s", id);
    }

    @GetMapping("/answer/delete/{id}")
    public String delete(@PathVariable int id) {
        Answer answer = answerService.getAnswer(id);
        Question question = answer.getQuestion();
        answerService.delete(id);
        return String.format("redirect:/question/detail/%d", question.getId());
    }
}
