package _thBackEnd.LectureCode.repository;

import _thBackEnd.LectureCode.domain.Article;
import _thBackEnd.LectureCode.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final EntityManager em;
    private final MemberRepository memberRepository;

    @Override
    public Article addArticle(Article article) {
        em.persist(article);
        return article;
    }

    @Override
    public void deleteArticle(Article article) {
        em.remove(article);
    }

    @Override
    public Article findById(Long articleId) {
        return em.find(Article.class, articleId);
    }

    @Override
    public List<Article> findAll() {
        return em.createQuery("select a from Article a", Article.class).getResultList();
    }

    @Override
    public List<Article> findUserAll(Long memberId) {
        Member member = memberRepository.findById(memberId);
        return em.createQuery("select a from Article a where a.writer = :m", Article.class)
                .setParameter("m", member).getResultList();
    }
}
