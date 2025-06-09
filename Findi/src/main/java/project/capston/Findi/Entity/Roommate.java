package project.capston.Findi.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Roommate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String birth;
    private String student_id;
    private String mbti;
    private String major;
    private int gender;
    private int is_Smoking;
    private int life_pattern;
}
