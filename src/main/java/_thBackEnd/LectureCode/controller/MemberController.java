package _thBackEnd.LectureCode.controller;

import _thBackEnd.LectureCode.DTO.MemberDTO;
import _thBackEnd.LectureCode.domain.Member;
import _thBackEnd.LectureCode.exception.MemberException;
import _thBackEnd.LectureCode.security.JwtUtility;
import _thBackEnd.LectureCode.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtility jwtUtility;

    @PostMapping("/member/add")
    public ResponseEntity<String> addMember(@RequestBody MemberDTO.MemberCreateReq request) {
        Member member = memberService.singUp(request.getUserId(), request.getPassword(), request.getNickname());
//        if (member == null) { // member가 null이 될 경우의 수가 없으므로 삭제
//            return null;
//        }
        return ResponseEntity.ok(jwtUtility.generateToken(member.getUserId()));
    }

    @PostMapping("/member/login")
    public ResponseEntity<String> login(@RequestBody MemberDTO.LoginReq request){
        Member member = memberService.login(request.getUserId(), request.getPassword());
//        if (member == null) { // member가 null이 될 경우의 수가 없으므로 삭제
//            return null;
//        }
        return ResponseEntity.ok(jwtUtility.generateToken(member.getUserId()));
    }

    @GetMapping("/member/{userId}")
    public ResponseEntity<MemberDTO.MemberRes> getMember(@PathVariable("userId") String userId){
        Member member = memberService.findByUserId(userId);
        return ResponseEntity.ok(new MemberDTO.MemberRes(member.getUserId(),member.getNickname()));
    }

    @PutMapping("/member")
    public MemberDTO.MemberRes changeMemberName(@RequestHeader("Authorization") String token, @RequestBody MemberDTO.MemberUpdateReq request){
        if (!jwtUtility.validateToken(token)) {
            return null;
        }
        Member findMember = memberService.changeName(request.getUserId(), request.getNickname());
        return new MemberDTO.MemberRes(findMember.getUserId(),findMember.getNickname());
    }

    @DeleteMapping("/member")
    public void deleteMember(@RequestHeader("Authorization") String token, @RequestBody MemberDTO.DeleteReq request){
        if (!jwtUtility.validateToken(token)) {
            return ;
        }
        memberService.deleteMember(request.getUserId());
    }
}
