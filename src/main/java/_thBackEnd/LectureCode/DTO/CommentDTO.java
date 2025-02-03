package _thBackEnd.LectureCode.DTO;

import _thBackEnd.LectureCode.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class CommentDTO {

    @Data
    public static class CommentCreateReq {
        @Schema(description = "게시글 id", example = "1")
        private Long articleId;
        @Schema(description = "댓글 내용", example = "취직하고 싶다.")
        private String content;
    }

    @Data
    public static class CommentUpdateReq {
        private Long commentId;
        private String Content;
    }

    @Data
    public static class CommentDeleteReq {
        private Long commentId;
    }

    @Data
    public static class ResComment {
        @Schema(description = "댓글 내용", example = "취직하고 싶다.")
        private String content;
        @Schema(description = "댓글 작성 시간", example = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        private LocalDateTime createDate;
        @Schema(description = "수정 여부", example = "True or False")
        private boolean isUpdate;
        @Schema(description = "댓글 작성자", example = "한민규")
        private String writer;

        public ResComment(Comment comment){
            this.content = comment.getContent();
            this.createDate = comment.getCreateDate();
            this.isUpdate = (comment.getCreateDate().equals(comment.getUpdateDate())) ? false : true;
            this.writer = comment.getWriter().getNickname();
        }
    }
}
