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
        return memberRepository.findByUserId(jwtUtility.validateToken(token).getSubject());
    }

    public Member changeName(Long id, String newNickname) {
        Member member = memberRepository.findByid(id);
        if (member == null) {
            return null;
        }
        member.setNickname(newNickname);
        return member;
    }

    public Member singUp(String userId, String password) {
        if (memberRepository.findByUserId(userId) != null) {
            return null;
        }
        return memberRepository.save(new Member(userId, password));
    }

    public Member login(String userId, String password) {
        Member member = memberRepository.findByUserId(userId);
        if (member != null && member.checkPassword(password)) {
            return member;
        }
        return null;
    }

    public boolean deleteMember(Long id) {
        Member member = memberRepository.findByid(id);
        if (member == null) {
            return false;
        }
        memberRepository.deleteMember(member);
        return true;
    }
}
