package _thBackEnd.LectureCode.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtility {

    private final Key key; // JWT 서명에 사용되는 비밀 키

    private static final long expirationTime = 1000 * 60 * 60; // 토큰 만료 시간: 1시간

    // 비밀 키 생성
    public JwtUtility() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 키 생성
    }

    // JWT 생성
    public String generateToken(String userId) {
        System.out.println(userId);
        return Jwts.builder()
                .setSubject(userId) // 토큰에 사용자 ID 저장
                .setIssuedAt(new Date()) // 토큰 생성 시간(시점) 설정
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 토큰 만료 시간 설정
                .signWith(key) // 비밀 키로 서명
                .compact(); // 토큰 생성 후 반환
    }

    // JWT 유효성 검사
    public Boolean validateToken(String bearerToken) {
        try {
            // 1. Bearer 검증
            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                System.out.println("Bearer로 시작하지 않음");
                return false; // 올바르지 않은 형식일 경우
            }
            System.out.println("받은 Bearer 토큰: " + bearerToken);
            System.out.println("파싱된 토큰: " + bearerToken.substring(7));

            String token = bearerToken.substring(7); // "Bearer " 제거 후 실제 토큰만 추출

            // 토큰 서명 및 유효성 검증
            Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 확인을 위한 키 설정
                    .build()
                    .parseClaimsJws(token);  // 토큰 파싱 및 검증

            return true; // 유효한 토큰일 경우 true
        } catch (ExpiredJwtException e) {
            System.out.println("토큰이 만료되었습니다: " + e.getMessage());
            return false;
        }
        catch (JwtException e) {
            System.out.println("Jwt 오류");
            return false; // 서명 검증 실패, 만료, 형식 오류 등이 발생한 경우 false
        }
    }

    // 토큰에서 클레임 추출
    public Claims getClaimsFromToken(String token) {
        String newToken = token.substring(7); //"Bearer " 제거 후 실제 토큰만 추출
        // JWT 토큰에서 정보를 추출 (예: userId, 생성 시간 등)
        return Jwts.parserBuilder()
                .setSigningKey(key) // 서명 확인을 위한 키 설정
                .build()
                .parseClaimsJws(newToken) // 토큰 파싱 및 검증
                .getBody();  // 토큰의 페이로드에서 클레임 데이터 반환
    }
}
