package project.capston.Findi.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDate createDate;

    @ManyToOne
    //답변 여러 개, 질문 게시글은 하나
    private Question question;

    @ManyToOne
    private Member author;
}
