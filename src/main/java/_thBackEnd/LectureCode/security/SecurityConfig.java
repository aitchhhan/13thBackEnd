package _thBackEnd.LectureCode.security;

import _thBackEnd.LectureCode.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Spring의 설정 클래스임을 나타냄
@EnableWebSecurity // Spring Security를 활성화하고, 우리가 설정한 보안 설정을 적용
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtility jwtUtility;
    private final CustomUserDetailsService customUserDetailsService;

    // @Bean은 메서드 레벨에서 직접 객체를 등록할 때 사용
    @Bean // Spring Security의 보안 설정을 구성하는 SecurityFilterChain을 정의하는 메서드 // 파라미터로 HttpSecurity 객체를 받아 보안 정책을 설정
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable) // Spring Security의 기본 인증 방식인 Basic Authentication을 비활성화
                .csrf(AbstractHttpConfigurer::disable) // JWT는 CSRF 공격에 취약하지 않아 CSRF 보호 비활성화 // 보통 CSRF 보호는 세션 기반 인증을 위해 사용
                .formLogin(AbstractHttpConfigurer::disable) //  Spring Security의 기본 폼 로그인 기능을 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT를 사용하기 때문에 세션을 사용하지 않도록 STATELESS로 설정
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger UI와 API 문서화 경로에 대한 접근을 모든 사용자에게 허용
                                .requestMatchers("/member/add", "/member/login").permitAll() // 회원가입, 로그인은 인증 없이 접근 가능
                                .requestMatchers("/article/**").hasRole("MEMBER") // 해당 url에 대한 접근은 "MEMBER"라는 role을 가지고 있어야 함
                                .requestMatchers("/comment/**").hasAnyRole("ADMIN", "MEMBER") // 해당 url에 대한 접근은 "ADMIN", "MEMBER"라는 role들을 가지고 있어야 함
                                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                // Spring Security의 UsernamePasswordAuthenticationFilter 실행 전에 JwtAuthenticationFilter를 실행하도록 설정하여 모든 요청에서 JWT 검증이 이루어지고, 유효한 JWT면 인증 정보를 설정
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtility, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);
                                                                                                    // UsernamePasswordAuthenticationFilter란? -> Spring Security에서 기본적으로 제공하는 로그인 인증 필터
        return http.build(); // 설정이 완료된 HttpSecurity 객체를 빌드하여 SecurityFilterChain을 반환하여, 해당 설정이 Spring Security의 보안 필터로 동작하도록 함
                             // 이제 Spring Security는 JWT 인증을 적용한 상태로 모든 요청을 처리함.
    }

}
