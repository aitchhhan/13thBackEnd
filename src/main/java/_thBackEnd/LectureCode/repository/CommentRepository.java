package _thBackEnd.LectureCode.repository;

import _thBackEnd.LectureCode.domain.Article;
import _thBackEnd.LectureCode.domain.Comment;
import _thBackEnd.LectureCode.domain.Member;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment addComment(Comment comment);
    Comment findById(Long id);
    void deleteComment(Comment comment);
    List<Comment> findArticleComment(Article article);
}
