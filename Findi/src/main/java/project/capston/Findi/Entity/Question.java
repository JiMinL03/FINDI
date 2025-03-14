package project.capston.Findi.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //데이터를 저장할 때 해당 속성에 값이 자동으로 1씩 증가함
    private int id;

    @Column(length = 200) //글자 수 200으로 제한
    private String subject;

    @Column(columnDefinition = "TEXT") //글자 수 제한 x
    private String content;

    @Lob
    @Column(name = "img")
    private byte[] img;

    private LocalDate createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    //질문 게시글 하나에 답변 갯수 여러 개
    //만약 질문 게시글이 삭제되면 답변리스트도 같이 삭제됨
    private List<Answer> answerList;

    @ManyToOne
    private Member author;
}
