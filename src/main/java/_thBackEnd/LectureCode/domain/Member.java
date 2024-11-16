package _thBackEnd.LectureCode.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
public class Member {
    private Long id;
    private String userId;
    private String password;
    @Setter
    private String nickname;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Member(String userId, String password) {
        this.userId = userId;
        this.setPassword(password);
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
