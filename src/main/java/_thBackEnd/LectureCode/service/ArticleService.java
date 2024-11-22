package _thBackEnd.LectureCode.service;

import _thBackEnd.LectureCode.domain.Article;
import _thBackEnd.LectureCode.domain.Member;
import _thBackEnd.LectureCode.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberService memberService;

    @Transactional
    public Article addArticle(String writerId, String title, String content) {
        Member member = memberService.findByUserId(writerId);
        Article article = new Article(title, content, member);
        articleRepository.addArticle(article);
        return article;
    }

    @Transactional
    public Article updateArticle(Long articleId, String title, String content, String token) {
        Article article = articleRepository.findById(articleId);
        Member member = memberService.tokenToMember(token);
        if (article.getWriter() == member) {
            article.update(title, content);
        }
        return article;
    }

    @Transactional
    public void deleteArticle(Long articleId, String token) {
        Article article = articleRepository.findById(articleId);
        Member member = memberService.tokenToMember(token);
        if (article.getWriter() == member) {
            articleRepository.deleteArticle(article);
        }
    }

    public Article findArticle(Long articleId){
        return articleRepository.findById(articleId);
    }

    public List<Article> findAllArticle(){
        return articleRepository.findAll();
    }

    public List<Article> findUserArticles(String memberId){
        Member member = memberService.findByUserId(memberId);
        return articleRepository.findUserAll(member.getId());
    }
}
