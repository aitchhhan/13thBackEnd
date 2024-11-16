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
        return jwtUtility.generateToken(member.getUserId());
    }

    @PostMapping("/member/login")
    public String login(@RequestBody MemberDTO.LoginReq request){
        Member member = memberService.login(request.getUserId(), request.getPassword());
        if (member != null) {
            return jwtUtility.generateToken(member.getUserId());
        }
        return null;
    }

    @GetMapping("/member/{userId}")
    public MemberDTO.MemberRes getMember(@PathVariable("userId") String userId){
        Member member = memberService.findByUserId(userId);
        return new MemberDTO.MemberRes(member.getUserId(),member.getNickname());
    }

    @PutMapping("/member")
    public MemberDTO.MemberRes changeMemberName(@RequestHeader("Authorization") String bearerToken, @RequestBody MemberDTO.MemberUpdateReq request){
        Member findMember = memberService.changeName(bearerToken, request.getUserId(), request.getNickname());
        return new MemberDTO.MemberRes(findMember.getUserId(),findMember.getNickname());
    }

    @DeleteMapping("/member")
    public void deleteMember(@RequestHeader("Authorization") String bearerToken, @RequestBody MemberDTO.DeleteReq request){
        memberService.deleteMember(bearerToken, request.getUserId());
    }
}
