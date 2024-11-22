package _thBackEnd.LectureCode.service;

import _thBackEnd.LectureCode.domain.Member;
import _thBackEnd.LectureCode.repository.MemberRepository;
import _thBackEnd.LectureCode.security.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtility jwtUtility;

    public Member tokenToMember(String token) {
        return memberRepository.findByUserId(jwtUtility.getClaimsFromToken(token).getSubject());
    }

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
        if (member == null || !member.checkPassword(password)) {
            return null;
        }
        return member;
    }


    public Member changeName(String userId, String newNickname) {
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

    public boolean deleteMember(String userId) {
        Member member = memberRepository.findByUserId(userId);
        if (member == null) {
            return false;
        }
        memberRepository.deleteMember(member);
        return true;
    }
}
