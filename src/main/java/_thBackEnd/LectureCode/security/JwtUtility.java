package _thBackEnd.LectureCode.security;

import _thBackEnd.LectureCode.exception.InvalidJwtException;
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
    public void validateToken(String bearerToken) {
            // 1. Bearer 검증
        try {
            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                throw new JwtException("JWT 검증 중 잘못된 인수가 전달되었습니다.");
            }

            String pureToken = bearerToken.substring(7); // Bearer 제거

            // 토큰 서명 및 유효성 검증
            Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 키 설정
                    .build()
                    .parseClaimsJws(pureToken); // 서명이 유효하지 않거나 변조되었으면 예외 발생
        } catch (JwtException e) {
            throw new JwtException("JWT 검증 실패: " + e.getMessage());
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
