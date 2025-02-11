package _thBackEnd.LectureCode.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
@Entity
public class Member {
    @Id @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String userId;
    private String password;
    @Setter
    private String nickname;

    @Enumerated(EnumType.STRING) // 기본적으로 enum 값은 숫자로 저장되지만, 이를 문자열로 저장하도록
    private RoleType roleType;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Member(String userId, String password) {
        this.userId = userId;
        this.setPassword(password);
        this.roleType = RoleType.MEMBER; // 회원가입시 기본적으로 Role을 MEMBER로 설정
    }

    public void setPassword(String password) {
        this.password = passwordEncoding(password);
    }

    public String passwordEncoding(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean checkPassword(String rawPassword) {
        return passwordEncoder.matches(rawPassword, this.password);
    }
}
