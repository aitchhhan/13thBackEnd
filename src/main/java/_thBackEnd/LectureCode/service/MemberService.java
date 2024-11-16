package _thBackEnd.LectureCode.service;

import _thBackEnd.LectureCode.domain.Member;
import _thBackEnd.LectureCode.repository.MemberRepository;
import _thBackEnd.LectureCode.security.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtility jwtUtility;

    public Member tokenToMember(String token){
        return memberRepository.findByUserId(jwtUtility.getClaimsFromToken(token).getSubject());
    }

    @Transactional
    public Member singUp(String userId, String password, String nickname) {
        if (memberRepository.findByUserId(userId) != null) {
            return null;
        }
        Member member = new Member(userId, password);
        member.setNickname(nickname);
        memberRepository.save(member);
        return member;
    }

    public Member login(String userId, String password) {
        Member member = memberRepository.findByUserId(userId);
        if (member != null && member.checkPassword(password)) {
            return member;
        }
        return null;
    }

    @Transactional
    public Member changeName(String token, String userId, String newNickname) {
        if (!jwtUtility.validateToken(token)) {
            return null;
        }
        Member member = memberRepository.findByUserId(userId);
        if (member == null) {
            return null;
        }
        member.setNickname(newNickname);
        return member;
    }

    public Member findByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }

    @Transactional
    public boolean deleteMember(String token, String userId) {
        if (!jwtUtility.validateToken(token)) {
            return false;
        }
        Member member = memberRepository.findByUserId(userId);
        if (member == null) {
            return false;
        }
        memberRepository.deleteMember(member);
        return true;
    }
}
