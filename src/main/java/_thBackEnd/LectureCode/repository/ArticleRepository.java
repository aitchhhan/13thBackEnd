package _thBackEnd.LectureCode.repository;

import _thBackEnd.LectureCode.domain.Article;

import java.util.List;

public interface ArticleRepository {

    Article addArticle(Article article);
    void deleteArticle(Article article);
    Article findById(Long articleId);
    List<Article> findAll();
    List<Article> findUserAll(Long memberId);

}
