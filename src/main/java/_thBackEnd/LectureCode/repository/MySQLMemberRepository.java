package _thBackEnd.LectureCode.repository;

import _thBackEnd.LectureCode.domain.Member;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MySQLMemberRepository implements MemberRepository {
    //@PersistenceContext
    //private EntityManager entityManager;
    @Override
    public Member save(Member member) {
        //entityManager.persist(member);
        return member;
    }

    @Override
    public Member findById(Long id) {
        return null;
    }
    @Override
    public Member findByUserId(String userId) {
        String jpql = "SELECT m FROM Member m WHERE m.userId = :userId";
        //List<Member> result = entityManager.createQuery(jpql, Member.class)
        //        .setParameter("userId", userId)
        //        .getResultList();
        //return result.isEmpty() ? null : result.get(0);
        return null;
    }

    @Override
    public List<Member> findAll() {
        return List.of();
    }

    @Override
    public void deleteMember(Member member) {

    }

    @Override
    public List<Member> findByName(String name) {
        return List.of();
    }
}
