package _thBackEnd.LectureCode.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtility {
    private final String secret = "yourSecretKey";

    private static final long expirationTime = 1000 * 60 * 60; // 1시간

    // JWT 생성
    public String generateToken(String memberId) {
        return Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    // JWT 유효성 검사
    public Claims validateToken(String token) {
        try {
            // 토큰 파싱 및 클레임 반환
            return Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            // JWT 토큰 잘못된 서명
            System.out.println("SignatureException");
        } catch (MalformedJwtException e) {
            // 잘못된 형식의 JWT 토큰
            System.out.println("MalformedJwtException");
        } catch (ExpiredJwtException e) {
            // 만료된 JWT 토큰
            System.out.println("ExpiredJwtException");
        } catch (UnsupportedJwtException e) {
            // 지원하지 않는 JWT 토큰
            System.out.println("UnsupportedJwtException");
        } catch (IllegalArgumentException e) {
            // JWT 토큰 claims이 비어 있음
            System.out.println("IllegalArgumentException");
        }
        return null; // 유효하지 않은 경우 null 반환
    }
}
