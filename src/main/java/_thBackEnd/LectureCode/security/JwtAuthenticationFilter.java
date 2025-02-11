package _thBackEnd.LectureCode.security;

import _thBackEnd.LectureCode.exception.HandleJwtException;
import _thBackEnd.LectureCode.service.CustomUserDetailsService;
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
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = resolveJwt(request); // HttpServletRequest 헤더에서 JWT 추출
            if (jwt != null && jwtUtility.validateJwt(jwt)) { // 유효한 JWT 토큰 반환시
                Authentication auth = getAuthentication(jwt); // 인증 객체 생성

                // Spring Security의 SecurityContext에 인증 정보를 저장하여 이후 요청에서 인증된 사용자로 인식할 수 있도록 함.
                SecurityContextHolder.getContext().setAuthentication(auth); // Controller, Service에서 현재 인증된 사용자 정보를 가져올 수도 있음
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
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader.substring(7); // "Bearer " 부분을 제거하고 JWT만 반환
    }

    // 인증 객체 생성
    private Authentication getAuthentication(String jwt) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtUtility.getClaimsFromJwt(jwt).getSubject());

        // UsernamePasswordAuthenticationToken은 Spring Security의 Authentication(인증 객체) 구현체.
        return new UsernamePasswordAuthenticationToken(userDetails, jwt, userDetails.getAuthorities()); // userDetails, JWT, 권한으로 인증 객체 생성
    }
}




