package _thBackEnd.LectureCode.service;

import _thBackEnd.LectureCode.domain.Member;
import _thBackEnd.LectureCode.exception.MemberException;
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
            throw new MemberException(409, "이미 있는 userId");
        }
        Member member = new Member(userId, password);
        member.setNickname(nickname);
        memberRepository.save(member);
        return member;
    }

//    public Member login(String userId, String password) {
//        Member member = memberRepository.findByUserId(userId);
//        if (member != null && member.checkPassword(password)) {
//            return member;
//        }
//    }

    public String login(String userId, String password) {
        Member member = memberRepository.findByUserId(userId);
        if (member == null || !member.checkPassword(password)) {
            // 딮하게 예외 처리를 한다면 || 연산으로 묶지 않고 따로 따로 예외 처리를 해주면 됨
            throw new MemberException(400, "존재하지 않거나, 틀린 비밀번호");
        }
        return jwtUtility.generateToken(member.getUserId());
    }

    @Transactional
    public Member changeName(String token, String newNickname) {
        Member member = tokenToMember(token); // 본인만 닉네임 바꿀 수 있게 토큰에서 member 추출
        if (member == null) { // 어쩌면 이 부분은 tokenToMember에서 오류 처리를 하는게 좋을지도?
            throw new MemberException(400, "없는 userId");
        }
        member.setNickname(newNickname);
        return member;
    }

    public Member findByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }

    @Transactional
    public boolean deleteMember(String userId) {
        Member member = memberRepository.findByUserId(userId);
        if (member == null) {
            throw new MemberException(400, "없는 userId");
        }
        memberRepository.deleteMember(member);
        return true;
    }
}
