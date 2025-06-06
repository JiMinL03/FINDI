package project.capston.Findi.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Roommate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer gender;
    private String name;
    private String birth;
    private String student_id;
    private String mbti;
    private String major;
    private Integer is_Smoking;
    private Integer life_pattern;
}
