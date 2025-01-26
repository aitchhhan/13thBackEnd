package _thBackEnd.LectureCode.service;

import _thBackEnd.LectureCode.domain.Member;
import _thBackEnd.LectureCode.repository.MemberRepository;
import _thBackEnd.LectureCode.security.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member signUp(String userId, String password, String nickname) {
        if (memberRepository.findByUserId(userId) != null) {
            return null;
        }
        Member member = new Member(userId, password);
        member.setNickname(nickname);
        memberRepository.save(member);
        return member;
    }

    private final JwtUtility jwtUtility;

    public String login(String userId, String password) {
        Member member = memberRepository.findByUserId(userId);

        if (member == null || !member.checkPassword(password)) {
            return null;
        }
        return jwtUtility.generateToken(member.getUserId());
    }


    public Member tokenToMember(String token) {
        return memberRepository.findByUserId(jwtUtility.getClaimsFromToken(token).getSubject());
    }

    public Member changeName(String token, String newNickname) {
        Member member = tokenToMember(token); // 본인만 닉네임 바꿀 수 있게 토큰에서 member 추출
        if (member == null) {
            return null;
        }
        member.setNickname(newNickname);
        List<Member> members = memberRepository.findAll();
        System.out.println("회원 목록: " + members);
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
