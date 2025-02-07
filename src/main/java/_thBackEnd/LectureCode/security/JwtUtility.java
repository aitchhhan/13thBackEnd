package _thBackEnd.LectureCode.security;

import _thBackEnd.LectureCode.domain.RoleType;
import _thBackEnd.LectureCode.exception.InvalidJwtException;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import java.util.Base64;
import io.jsonwebtoken.security.Keys;


import java.util.Date;

@Service
public class JwtUtility {

    private final SecretKey secretKey; // JWT 서명에 사용되는 비밀 키 // 생성한 비밀 키의 타입이 SecretKey 타입

    private static final long expirationTime = 1000 * 60 * 60; // 밀리초 단위 // 토큰 만료 시간: 1시간

    // JWT 서명에 사용되는 비밀 키 생성
    public JwtUtility(@Value("${jwt.base64Secret}") String base64Secret) { // @Value을 통해 application.yml에서 값 주입
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret); // Base64로 인코딩된 문자열을 디코딩하여 바이트 배열로 변환
        this.secretKey = Keys.hmacShaKeyFor(decodedKey); // Keys.hmacShaKeyFor()는 JWT 서명을 위한 SecretKey 타입 비밀 키 객체를 반환
    }                                                    // base64Secret이 64바이트 이상이면 자동으로 HS512 알고리즘 사용

    // JWT 생성
    public String generateToken(String userId, RoleType roleType) {
        return Jwts.builder()
                .setSubject(userId) // 토큰의 주체로 userId 설정
                .claim("RoleType", roleType) // 클레임에 roleType 추가
                .setIssuedAt(new Date()) // 토큰 생성 시점 설정
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 토큰 만료 시간 설정
                .signWith(secretKey, SignatureAlgorithm.HS512) // 비밀 키로 서명 // 알아서 HS512 알고리즘을 사용하지만 명확하게 지정하는 것이 좋음
                .compact(); // 토큰 생성 후 반환
    }

    // JWT 유효성 검사
    public Boolean validateToken(String bearerToken) {
        try {
            // 토큰이 null 값이거나 "Bearer "로 시작하지 않는 경우
            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                return false;
            }

            String token = bearerToken.substring(7); // "Bearer " 제거 후 실제 토큰만 추출

            // 토큰 서명 및 유효성 검증
            Jwts.parserBuilder()
                    .setSigningKey(secretKey) // 서명 확인을 위한 키 설정
                    .build()
                    .parseClaimsJws(token);  // 토큰 파싱 및 검증
            return true; // 유효한 토큰일 경우 true
        } catch (ExpiredJwtException e) {
            return false;
        }
        catch (JwtException e) {
            return false; // 서명 검증 실패, 만료, 형식 오류 등이 발생한 경우 false
        }
    }

    // 토큰에서 클레임 추출
    public Claims getClaimsFromToken(String bearerToken) {
        String token = bearerToken.substring(7); //"Bearer " 제거 후 실제 토큰만 추출
        // JWT 토큰에서 정보를 추출 (예: userId, 생성 시간 등)
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // 서명 확인을 위한 키 설정
                .build()
                .parseClaimsJws(token) // 토큰 파싱 및 검증
                .getBody();  // 토큰의 페이로드에서 클레임 데이터 반환
    }
}
