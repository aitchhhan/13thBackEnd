package _thBackEnd.LectureCode.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Spring의 설정 클래스임을 나타냄
public class WebConfig implements WebMvcConfigurer { // WebMvcConfigurer를 구현하여 웹 관련 설정을 커스터마이징

    @Override                   // CorsRegistry는 CORS를 설정할 때 사용하는 객체
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로(/**)에 대해 CORS 설정 적용
                .allowedOrigins("http://localhost:3000", "https://프론트배포도메인.com") // CORS 요청을 허용할 특정 도메인(origin)을 지정
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드를 지정
                .allowedHeaders("*") // 클라이언트가 모든 HTTP 헤더를 보낼 수 있게 허용
                .allowCredentials(true); // 기본적으로 브라우저는 CORS 요청을 보낼 때 쿠키나 인증 정보를 포함하지 않으므로 true로 설정하여
    }                                    //  클라이언트가 요청 시 쿠키, 세션, 인증 정보(Cookie, Authorization 헤더 등)를 포함할 수 있도록 허용
}
