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
                .setSubject(userId) // jwt 주체를 userId로 설정
                .setIssuedAt(new Date()) // 토큰 생성 시간(시점) 설정
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 토큰 만료 시간 설정
                .signWith(key) // Key 객체를 직접 사용
                .compact(); // 토큰 생성 및 압축
    }

    // JWT 유효성 검사
    public Boolean validateToken(String bearerToken) {
        try {
            // 1. Bearer 검증
            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                System.out.println("Bearer로 시작하지 않음"); // exception을 아직 안 배워서 어떤 오류인지 Console에서 볼 수 있게
                return false; // 토큰이 null 이거나, Bearer로 시작하지 않을 경우 false
            }

            String token = bearerToken.substring(7); // Bearer 제거

            // 토큰 서명 및 유효성 검증
            Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 키 설정
                    .build()
                    .parseClaimsJws(token); // 서명이 유효하지 않거나 변조되었으면 예외 발생

            return true; // 유효한 토큰일 경우 true
        } catch (JwtException e) {
            System.out.println("Jwt 오류"); // exception을 아직 안 배워서 어떤 오류인지 Console에서 볼 수 있게
            return false; // 서명 검증 실패, 만료, 형식 오류 등이 발생한 경우 false
        }
    }

    // 토큰에서 클레임 추출
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder() // 이러한 형태는 JWT에서 클레임을 추출하는 표준적인 방식
                .setSigningKey(key) // jwt 서명을 검증하기 위해 필요한 비밀 키 설정, jwt를 생성하며 서명할 때 사용했던 키와 동일해야 함
                .build() // 설정된 파서를 실제로 사용할 수 있는 파서 객체로 만듬
                .parseClaimsJws(token) // jwt 토큰 파싱하고, 서명의 유효성 검증
                .getBody(); // 파싱된 jwt의 Payload에서 Claim 추출
    }
}
