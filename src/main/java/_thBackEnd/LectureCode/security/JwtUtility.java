package _thBackEnd.LectureCode.security;

import _thBackEnd.LectureCode.domain.RoleType;
import _thBackEnd.LectureCode.exception.HandleJwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtUtility {

    private final SecretKey secretKey; // JWT 서명에 사용되는 비밀 키 // 생성한 비밀 키의 타입이 SecretKey 타입

    private static final long expirationTime = 1000 * 60 * 60; // 밀리초 단위 // JWT 만료 시간: 1시간

    // JWT 서명에 사용되는 비밀 키 생성
    public JwtUtility(@Value("${jwt.base64Secret}") String base64Secret) { // @Value을 통해 application.yml에서 값 주입
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret); // Base64로 인코딩된 문자열을 디코딩하여 바이트 배열로 변환
        this.secretKey = Keys.hmacShaKeyFor(decodedKey); // Keys.hmacShaKeyFor()는 JWT 서명을 위한 SecretKey 타입 비밀 키 객체를 반환
    }                                                    // base64Secret에 64qkdlxm 이상이면 자동으로 HS512 알고리즘 사용

    // JWT 생성
    public String generateJwt(String userId, RoleType roleType) {
        return Jwts.builder()
                .setSubject(userId) // JWT의 주체로 userId 설정
                .claim("role", roleType.name()) // 클레임에 roleType 추가 // Key의 이름을 "role"로 하기!
                .setIssuedAt(new Date()) // JWT 생성 시점 설정
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // JWT 만료 시간 설정
                .signWith(secretKey, SignatureAlgorithm.HS512) // 비밀 키로 서명 // 알아서 HS512 알고리즘을 사용하지만 명확하게 지정하는 것이 좋음
                .compact(); // JWT 생성 후 문자열로 반환
    }

    // JWT 유효성 검사
    public boolean validateJwt(String jwt) {
        try {
            Jwts.parserBuilder() // JWT를 파싱하고 검증하는 JwtParser 객체를 만들기 위한 설정을 담는 객체 생성
                    .setSigningKey(secretKey) // JWT를 검증할 때 사용할 서명 키를 설정 // 서명할 때 사용한 비밀 키를 사용
                    .build() // JwtParserBuilder에 설정된 값들을 기반으로 JwtParser 객체 생성
                    .parseClaimsJws(jwt);  // JwtParser 객체의 parseClaimsJws()에 JWT를 넣어 검증 // 실질적으로 JWT가 유효한지 검증하는 과정
            return true; // 유효한 토큰일 경우 true
        } catch (ExpiredJwtException e) {
            throw new HandleJwtException("만료된 JWT");
        } catch (UnsupportedJwtException e) {
            throw new HandleJwtException("지원되지 않는 JWT 형식");
        } catch (MalformedJwtException e) {
            throw new HandleJwtException("손상된 JWT");
        } catch (SecurityException e) {
            throw new HandleJwtException("서명이 올바르지 않은 JWT");
        } catch (IllegalArgumentException e) {
            throw new HandleJwtException("JWT가 null이거나 빈 문자열임");
        } catch (JwtException e) {
            throw new HandleJwtException("기타 JWT관련 예외");
        }
    }

    // JWT에서 클레임 추출
    public Claims getClaimsFromJwt(String jwt) {
        String NoneBearerJwt = jwt;
        // "Bearer "로 시작하면
        if (jwt.startsWith("Bearer ")) {
            NoneBearerJwt = jwt.substring(7); // "Bearer " 부분을 제거
        }
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(NoneBearerJwt)
                .getBody();  // JWT의 페이로드에서 클레임 반환
    }
}
