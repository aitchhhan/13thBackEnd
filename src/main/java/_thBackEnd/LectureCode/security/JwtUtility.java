package _thBackEnd.LectureCode.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtility {
    private final String jwtKey = "1234567812345678123456781234567812345678123456781234567812345678";

    private static final long expirationTime = 1000 * 60 * 60; // 1시간

    // JWT 생성
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, jwtKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    // JWT 유효성 검사
    public Boolean validateToken(String token) {
        try {
            // 토큰 파싱 및 클레임 반환
            Jwts.parserBuilder()
                    .setSigningKey(jwtKey.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
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

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(token).getBody(); // 토큰을 파싱하여 클레임을 추출
    }
}
