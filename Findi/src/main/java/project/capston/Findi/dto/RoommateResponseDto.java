package project.capston.Findi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoommateResponseDto {
    private String status;
    private List<RecommendedUser> recommended;

    @Getter
    @Setter
    public static class RecommendedUser {
        private int gender;
        private String name;
        private String birth;
        private  String student_id;
        private String mbti;
        private String major;
        private int is_Smoking;
        private int life_pattern;
    }
}
