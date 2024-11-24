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
    public String addMember(@RequestBody MemberDTO.MemberCreateReq request) {
        Member member = memberService.singUp(request.getUserId(), request.getPassword(), request.getNickname());
        if (member == null) {
            return null;
        }
        return memberService.login(member.getUserId(), member.getPassword());
    }

    @PostMapping("/member/login")
    public String login(@RequestBody MemberDTO.LoginReq request){
        return memberService.login(request.getUserId(), request.getPassword());
    }

    @GetMapping("/member/{userId}")
    public MemberDTO.MemberRes getMember(@PathVariable("userId") String userId){
        Member member = memberService.findByUserId(userId);
        return new MemberDTO.MemberRes(member.getUserId(),member.getNickname());
    }

    @PutMapping("/member")
    public MemberDTO.MemberRes changeMemberName(@RequestHeader("Authorization") String token, @RequestBody MemberDTO.MemberUpdateReq request){
        if (!jwtUtility.validateToken(token)) {
            return null;
        }
        Member findMember = memberService.changeName(token, request.getNickname());
        return new MemberDTO.MemberRes(findMember.getUserId(),findMember.getNickname());
    }

    @DeleteMapping("/member")
    public Boolean deleteMember(@RequestHeader("Authorization") String token, @RequestBody MemberDTO.DeleteReq request){
        if (!jwtUtility.validateToken(token)) {
            return false;
        }
        return memberService.deleteMember(request.getUserId());
    }
}
