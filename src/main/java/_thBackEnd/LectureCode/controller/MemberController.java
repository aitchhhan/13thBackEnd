package _thBackEnd.LectureCode.controller;

import _thBackEnd.LectureCode.DTO.MemberDTO;
import _thBackEnd.LectureCode.domain.Member;
import _thBackEnd.LectureCode.security.JwtUtility;
import _thBackEnd.LectureCode.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtility jwtUtility;

    @PostMapping("/member/add")
    public String addMember(@RequestBody MemberDTO.MemberCreateReq req) {
        Member member = memberService.singUp(req.getUserId(), req.getPassword());
        if (member == null) {
            return null;
        }
        return jwtUtility.generateToken(member.getUserId());
    }

    @PostMapping("/login")
    public String Login(@RequestBody MemberDTO.LoginReq req){
        Member loginMember = memberService.login(req.getUserId(), req.getPassword());
        if(loginMember!=null){
            return jwtUtility.generateToken(loginMember.getUserId());
        }
        return null;
    }

    @GetMapping("/member/{userId}")
    public MemberDTO.MemberRes getMember(@PathVariable("userId") String userId){
        Member member = memberService.findByUserId(userId);
        return new MemberDTO.MemberRes(member.getUserId(),member.getNickname());
    }

    @PutMapping("/member")
    public MemberDTO.MemberRes changeMemberName(@RequestBody MemberDTO.MemberUpdateReq req){
        Member findMember = memberService.changeName(req.getId(), req.getNickname());
        return new MemberDTO.MemberRes(findMember.getUserId(),findMember.getNickname());
    }

    @DeleteMapping("/member")
    public void deleteMember(@RequestBody MemberDTO.DeleteReq req){
        memberService.deleteMember(req.getId());
    }
}
