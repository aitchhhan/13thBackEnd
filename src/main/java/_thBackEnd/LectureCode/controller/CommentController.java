package _thBackEnd.LectureCode.controller;

import _thBackEnd.LectureCode.DTO.CommentDTO;
import _thBackEnd.LectureCode.domain.Comment;
import _thBackEnd.LectureCode.security.JwtUtility;
import _thBackEnd.LectureCode.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "댓글 관련")
public class CommentController {

    private final CommentService commentService;
    private final JwtUtility jwtUtility;

    @Operation(summary = "댓글 생성", description = "Header에 token 필요, body에 json 형태로 게시물Id, 댓글 내용 필요",
            responses = {@ApiResponse(responseCode = "201", description = "댓글 생성"),
                    @ApiResponse(responseCode = "404", description = "없는 userId or 없는 articleId")})
    @PostMapping("/comment")
    public ResponseEntity<CommentDTO.ResComment> createComment(@RequestHeader("Authorization") String token, @RequestBody CommentDTO.CommentCreateReq request){
        jwtUtility.validateToken(token);
        Comment comment = commentService.saveComment(token, request.getArticleId(), request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentDTO.ResComment(comment));
    }

    @PutMapping("/comment")
    public ResponseEntity<CommentDTO.ResComment> updateComment(@RequestHeader("Authorization") String token, @RequestBody CommentDTO.CommentUpdateReq request){
        jwtUtility.validateToken(token);
        Comment comment = commentService.updateComment(request.getCommentId(), token, request.getContent());
        if(comment == null) return null;
        return ResponseEntity.status(HttpStatus.OK).body(new CommentDTO.ResComment(comment));
    }

    @GetMapping("/comment/article/{id}")
    public List<CommentDTO.ResComment> articleComment(@PathVariable("id") Long articleId) {
        return commentService.articleToComment(articleId) // 특정 게시글의 모든 댓글을 List<Comment>로 반환
                .stream() // Stream API를 사용하여 List<Comment>을 데이터를 흐름으로 처리할 수 있게 함
                .map(CommentDTO.ResComment::new) // Comment 객체를 CommentDTO.ResComment 객체로 변환
                .collect(Collectors.toList()); // 변환된 ResComment 객체들을 List로 수집(collect)하여 반환
    }                                          // 반환타입이 List<CommentDTO.ResComment>니까 이 타입으로 반환되겠죠?



    @DeleteMapping("/comment")
    public void deleteComment(@RequestHeader("Authorization") String token, @RequestBody CommentDTO.CommentDeleteReq request){
        jwtUtility.validateToken(token);
        commentService.deleteComment(request.getCommentId(), token);
    }
}