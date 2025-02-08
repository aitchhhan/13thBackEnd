package _thBackEnd.LectureCode.security;

import _thBackEnd.LectureCode.domain.RoleType;
import _thBackEnd.LectureCode.exception.HandleJwtException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = resolveJwt(request); // HttpServletRequest 헤더에서 JWT 추출
            if (jwt != null && jwtUtility.validateJwt(jwt)) { // 유효한 JWT 토큰 반환시
                Authentication auth = getAuthentication(jwt); // 인증 객체 생성
                if (auth != null) { // 유효한 인증 객체 반환시 // null인 경우는 userId가 null인 경우!

                    // Spring Security의 SecurityContext에 인증 정보를 저장하여 이후 요청에서 인증된 사용자로 인식할 수 있도록 함.
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            filterChain.doFilter(request, response); // 다음 필터로 요청 전달
        } catch (HandleJwtException e) { // validateJwt 메서드에서 발생한 예외
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized 반환
            response.getWriter().write(e.getMessage());
        }
    }

    // HttpServletRequest의 헤더에서 JWT 추출
    private String resolveJwt(HttpServletRequest request) {
        String authorizationHeader  = request.getHeader("Authorization"); //  Authorization 헤더(JWT) 추출

        // Authorization 헤더가 없거나 "Bearer "로 시작하지 않으면 null 반환
        if (authorizationHeader  == null || !authorizationHeader .startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader .substring(7); // "Bearer " 부분을 제거하고 JWT만 반환
    }

    // 인증 객체 생성
    private Authentication getAuthentication(String jwt) {
        Claims claims = jwtUtility.getClaimsFromJwt(jwt); // JWT에서 클레임 추출
        String userId = claims.getSubject(); // 클레임에서 JWT의 주체 추출
        if (userId == null) { // 클레임에서 추출한 JWT의 주체가 null이면
            return null; // null 반환
        }

        List<String> roles = claims.get("roles", List.class); // 클레임에 추가로 넣은 roles List로 추출

        // Role을 Spring Security GrantedAuthority(권한 객체)로 변환
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        // UsernamePasswordAuthenticationToken은 Spring Security의 Authentication(인증 객체) 구현체.
        return new UsernamePasswordAuthenticationToken(userId, jwt, authorities); // JWT 주체, JWT, 권한목록으로 인증 객체 생성
    }
}




