package _thBackEnd.LectureCode.service;

import _thBackEnd.LectureCode.domain.Article;
import _thBackEnd.LectureCode.domain.Comment;
import _thBackEnd.LectureCode.domain.Member;
import _thBackEnd.LectureCode.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final ArticleService articleService;

    @Transactional
    public Comment saveComment(String token, Long article_id, String content){
        Member member = memberService.tokenToMember(token);
        Article article = articleService.findArticle(article_id);
        Comment comment = new Comment(member, article, content);
        commentRepository.addComment(comment);
        return comment;
    }

    @Transactional
    public Comment updateComment(Long commentId, String token, String content){
        Comment comment = commentRepository.findById(commentId);
        Member member = memberService.tokenToMember(token);
        if(comment.getWriter() == member){
            comment.updateComment(content);
            return comment;
        }
        return null;
    }

    public List<Comment> articleToComment(Long articleId){
        Article article = articleService.findArticle(articleId);
        return commentRepository.findArticleComment(article);
    }

    @Transactional
    public boolean deleteComment(Long commentId, String token){
        Comment comment = commentRepository.findById(commentId);
        Member member = memberService.tokenToMember(token);
        if(comment.getWriter() == member){
            commentRepository.deleteComment(comment);
            return true;
        }
        return false;
    }
}
