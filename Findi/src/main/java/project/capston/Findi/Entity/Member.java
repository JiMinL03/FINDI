package project.capston.Findi.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @Column(unique = true)  // 로그인 시 사용할 고유 ID, 즉 id를 유니크로 설정
    private String id;

    private String password;

    private String username;

    private String job;

    @Lob
    @Column(name = "img", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] img;

    private boolean active; // 수락 여부 (true: 수락, false: 거절)

    private String email;

}
