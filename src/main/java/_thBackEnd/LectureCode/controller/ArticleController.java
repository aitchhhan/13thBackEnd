package _thBackEnd.LectureCode.controller;

import _thBackEnd.LectureCode.DTO.ArticleDTO;
import _thBackEnd.LectureCode.domain.Article;
import _thBackEnd.LectureCode.security.JwtUtility;
import _thBackEnd.LectureCode.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final JwtUtility jwtUtility;

    @PostMapping("/article/add")
    public ArticleDTO.ResponseArticle createArticle(@RequestHeader("Authorization") String token, @RequestBody ArticleDTO.addArticleReq request){
        jwtUtility.validateToken(token);
        String userId = jwtUtility.getClaimsFromToken(token).getSubject();
        Article article = articleService.addArticle(userId, request.getTitle(), request.getContent());
        return new ArticleDTO.ResponseArticle(article);
    }

    @PutMapping("/article/update")
    public ArticleDTO.ResponseArticle updateArticle(@RequestHeader("Authorization") String token, @RequestBody ArticleDTO.ArticleReq request){
        jwtUtility.validateToken(token);
        Article article = articleService.updateArticle(request.getArticleId(), request.getTitle(), request.getContent(), token);
        return new ArticleDTO.ResponseArticle(article);
    }

    @DeleteMapping("/article/{articleId}")
    public void deleteArticle(@RequestHeader("Authorization") String token, @PathVariable("articleId") Long articleId){
        jwtUtility.validateToken(token);
        articleService.deleteArticle(articleId, token);
    }

    @GetMapping("/article/{articleId}")
    public ArticleDTO.ResponseArticle getArticle(@PathVariable("articleId") Long articleId){
        Article article = articleService.findArticle(articleId);
        return new ArticleDTO.ResponseArticle(article);
    }

    @GetMapping("/articles/all")
    public List<ArticleDTO.ResponseArticle> allArticleList(){
        List<ArticleDTO.ResponseArticle> responseArticles = new ArrayList<>();
        for (Article article : articleService.findAllArticle()) {
            responseArticles.add(new ArticleDTO.ResponseArticle(article));
        }
        return responseArticles;
    }

    @GetMapping("/articles/all/{memberId}")
    public List<ArticleDTO.ResponseArticle> writerArticleList(@PathVariable("memberId") String memberId){
        List<ArticleDTO.ResponseArticle> responseArticles = new ArrayList<>();
        for (Article article : articleService.findUserArticles(memberId)) {
            responseArticles.add(new ArticleDTO.ResponseArticle(article));
        }
        return responseArticles;
    }
}
