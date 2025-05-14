package project.capston.Findi.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MemberProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //이름
    private String student_id; //학번
    private String gender; //성별
    private String major; //학과
    private String mbti; //mbti
    private int isSmoking; //흡연여부
    private String birth; //생년
    private int life_pattern; //생활패턴

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member; //Member 엔티티에서 id값을 외래키로 가지고 온다.
}
