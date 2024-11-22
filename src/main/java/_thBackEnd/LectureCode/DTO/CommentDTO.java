package _thBackEnd.LectureCode.DTO;

import _thBackEnd.LectureCode.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

public class CommentDTO {
    @Data
    public static class CommentCreateRequest {
        private String token;
        private Long articleId;
        private String content;
    }

    @Data
    public static class CommentUpdateRequest {
        private Long commentId;
        private String Content;
        private String token;
    }

    @Data
    public static class CommentDeleteRequest {
        private String token;
        private Long commentId;
    }

    @Data
    public static class CommentResponse{
        private String content;
        private LocalDateTime createDate;
        private boolean isUpdate;
        private String writer;
        private String writer_id;

        public CommentResponse(Comment comment){
            this.content = comment.getContent();
            this.createDate = comment.getCreateDate();
            this.isUpdate = (comment.getCreateDate().equals(comment.getUpdateDate())) ? false : true;
            this.writer = comment.getWriter().getNickname();
            this.writer_id = comment.getWriter().getUserId();
        }
    }
}
