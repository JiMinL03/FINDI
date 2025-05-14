package project.capston.Findi.Form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {
    @NotEmpty(message = "사용자 이메일은 필수 항목입니다.")
    private String email;
    
    @NotEmpty(message = "사용자 ID는 필수 항목입니다.")
    private String id;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
    private String password2;

    @NotEmpty(message = "닉네임은 필수 항목입니다.")
    private String username;

    private String job;
    private byte[] img;
}
