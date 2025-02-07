package _thBackEnd.LectureCode.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<String> MemberExceptionHandler(MemberException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 있는 userId");
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> JwtExceptionHandler(JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<String> InvalidUserId(InvalidUserIdException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("없는 userId");
    }

    @ExceptionHandler(InvalidArticleIdException.class)
    public ResponseEntity<String> InvalidArticleId(InvalidArticleIdException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("없는 articleId");
    }
}
