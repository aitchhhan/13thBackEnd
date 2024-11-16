package _thBackEnd.LectureCode.repository;

import _thBackEnd.LectureCode.domain.Member;

import java.util.List;

public interface MemberRepository {
    Member save(Member member);

    Member findByid(Long id);

    Member findByUserId(String userId);

    List<Member> findAll();

    void deleteMember(Member member);

    public List<Member> findByName(String name);
}
