package _thBackEnd.LectureCode.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

public class MemberDTO {

    @Data
    public static class MemberCreateReq {
        @Schema(description = "아이디", example = "testUserId")
        private String userId;
        @Schema(description = "비밀번호", example = "testPassword")
        private String password;
        @Schema(description = "닉네임", example = "testNickname")
        private String nickname;
    }

    @Data
    public static class MemberUpdateReq {
        private String nickname;
    }

    @Data
    @AllArgsConstructor
    public static class MemberRes {
        private String userId;
        private String nickname;
    }

    @Data
    public static class LoginReq {
        private String userId;
        private String password;
    }

    @Data
    public static class DeleteReq {
        private String userId;
    }
}
