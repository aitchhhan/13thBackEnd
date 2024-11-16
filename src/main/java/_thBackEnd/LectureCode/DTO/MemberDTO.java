package _thBackEnd.LectureCode.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

public class MemberDTO {

    @Data
    public static class MemberCreateReq {
        private String userId;
        private String password;
        private String nickname;
    }

    @Data
    public static class MemberUpdateReq {
        private String userId;
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
