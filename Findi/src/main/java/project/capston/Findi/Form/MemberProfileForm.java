package project.capston.Findi.Form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberProfileForm {
    @NotEmpty(message = "사용자 이름은 필수 항목입니다.")
    private String name;

    @NotEmpty(message = "사용자 학번은 필수 항목입니다.")
    private String student_id;

    @NotNull(message = "성별은 필수 항목입니다.")
    private Integer gender;

    @NotEmpty(message = "전공은 필수 항목입니다.")
    private String major;

    @NotEmpty(message = "mbti는 필수 항목입니다.")
    private String mbti;

    @NotNull(message = "흡연여부는 필수 항목입니다.")
    private Integer isSmoking;

    @NotEmpty(message = "출생연도는 필수 항목입니다.")
    private String birth;

    @NotNull(message = "생활패턴은 필수 항목입니다.")
    private Integer life_pattern;
}
