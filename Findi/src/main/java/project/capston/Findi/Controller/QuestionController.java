package project.capston.Findi.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import project.capston.Findi.Entity.Member;
import project.capston.Findi.Entity.Question;
import project.capston.Findi.Form.QuestionForm;
import project.capston.Findi.Service.MemberService;
import project.capston.Findi.Service.QuestionService;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final MemberService memberService;

    /*@GetMapping("/question")
    public String question(Model model) {
        List<Question> questionList = questionService.getList();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }*/

    @GetMapping("/question")
    public String question(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Question> paging = this.questionService.getPage(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);  // 검색어를 뷰에 다시 전달 (입력 유지용)
        return "question_list";
    }
    @GetMapping("/question/create")
    public String questionCreate(QuestionForm questionForm, Model model) {
        return "question_create";
    }

    @PostMapping("/question/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        //오류 발견, username이 두개로 반환됨!
        if(bindingResult.hasErrors()) {
            return "redirect:/question_create";
        }
        Member author = this.memberService.getMember(principal.getName());
        questionService.create(questionForm.getSubject(), questionForm.getContent(), null, author);
        return "redirect:/question";
    }

    @GetMapping(value = "/question/detail/{id}")
    public String questionDetail(@PathVariable int id, Model model) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @GetMapping("/question/delete/{id}")
    public String questionDelete(@PathVariable int id) {
        Question question = this.questionService.getQuestion(id);
        this.questionService.deleteQuestion(id);
        return "redirect:/question";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/modify/{id}")
    public String questionModify(@PathVariable int id, QuestionForm questionForm, Principal principal, Model model) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }
        model.addAttribute("question", question);
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question/modify/{id}")
    public String questionModify(@PathVariable int id, QuestionForm questionForm, Principal principal, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "question_modify";
        }
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }
        this.questionService.updateQuestion(question, questionForm.getSubject(), questionForm.getContent(), null);
        return String.format("redirect:/question/detail/%d", id);
    }


}
