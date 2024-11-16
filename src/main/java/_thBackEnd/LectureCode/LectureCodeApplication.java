package _thBackEnd.LectureCode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class LectureCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LectureCodeApplication.class, args);
	}

}
