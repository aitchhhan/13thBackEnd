package _thBackEnd.LectureCode.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException{

    private final int statusCode; // HTTP 상태 코드

    public MemberException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
