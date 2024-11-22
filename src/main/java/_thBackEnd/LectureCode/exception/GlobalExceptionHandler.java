package _thBackEnd.LectureCode.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<String> invalidUserId(MemberException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
    }
}
