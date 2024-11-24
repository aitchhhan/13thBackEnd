package _thBackEnd.LectureCode.controller;

import _thBackEnd.LectureCode.DTO.CommentDTO;
import _thBackEnd.LectureCode.domain.Article;
import _thBackEnd.LectureCode.domain.Comment;
import _thBackEnd.LectureCode.security.JwtUtility;
import _thBackEnd.LectureCode.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtUtility jwtUtility;

    @PostMapping("/comment")
    public CommentDTO.CommentResponse createComment(@RequestHeader("Authorization") String token, @RequestBody CommentDTO.CommentCreateRequest request){
        jwtUtility.validateToken(token);
        Comment comment = commentService.saveComment(request.getToken(), request.getArticleId(), request.getContent());
        return new CommentDTO.CommentResponse(comment);
    }

    @PutMapping("/comment")
    public CommentDTO.CommentResponse updateComment(@RequestHeader("Authorization") String token, @RequestBody CommentDTO.CommentUpdateRequest request){
        jwtUtility.validateToken(token);
        Comment comment = commentService.updateComment(request.getCommentId(), request.getToken(), request.getContent());
        if(comment == null) return null;
        return new CommentDTO.CommentResponse(comment);
    }

    @GetMapping("/comment/article/{id}")
    public List<CommentDTO.CommentResponse> articleComment(@PathVariable("id") Long articleId){
        List<CommentDTO.CommentResponse> response = new ArrayList<>();
        for(Comment comment : commentService.articleToComment(articleId)){
            response.add(new CommentDTO.CommentResponse(comment));
        }
        return response;
    }

    @DeleteMapping("/comment")
    public void deleteComment(@RequestHeader("Authorization") String token, @RequestBody CommentDTO.CommentDeleteRequest request){
        jwtUtility.validateToken(token);
        commentService.deleteComment(request.getCommentId(), token);
    }
}
