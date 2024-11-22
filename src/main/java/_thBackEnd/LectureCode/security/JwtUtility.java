package _thBackEnd.LectureCode.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtility {

    private final Key key; // Key 타입으로 변경

    private static final long expirationTime = 1000 * 60 * 60; // 1시간

    public JwtUtility() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 키 생성
    }

    // JWT 생성
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key) // Key 객체를 직접 사용
                .compact();
    }

    // JWT 유효성 검사
    public Boolean validateToken(String bearerToken) {
        try {
            // 1. Bearer 검증
            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                System.out.println("Bearer로 시작하지 않음");
                return false; // Bearer로 시작하지 않으면 유효하지 않음
            }

            String pureToken = bearerToken.substring(7); // Bearer 제거

            // 토큰 서명 및 유효성 검증
            Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 키 설정
                    .build()
                    .parseClaimsJws(pureToken); // 서명이 유효하지 않거나 변조되었으면 예외 발생

            return true; // 유효한 토큰일 경우 true
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Jwt 오류");
            return false; // 서명 검증 실패, 만료, 형식 오류 등이 발생한 경우 false
        }
    }

    // 토큰에서 클레임 추출
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
