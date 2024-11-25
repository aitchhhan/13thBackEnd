package _thBackEnd.LectureCode.controller;

import _thBackEnd.LectureCode.DTO.MemberDTO;
import _thBackEnd.LectureCode.domain.Member;
import _thBackEnd.LectureCode.security.JwtUtility;
import _thBackEnd.LectureCode.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtility jwtUtility;

    @Operation(summary = "회원가입", description = "아이디, 비밀전호, 닉네임 필요", tags = {"Member"},
    responses = {@ApiResponse(responseCode = "201", description = "Member 생성 후 token 반환 "),
    @ApiResponse(responseCode = "409", description = "아이디 중복으로 인한 오류")})
    @PostMapping("/member/add")
    public ResponseEntity<String> addMember(@RequestBody MemberDTO.MemberCreateReq request) {
        Member member = memberService.signUp(request.getUserId(), request.getPassword(), request.getNickname());
//        if (member == null) { // member가 null이 될 경우의 수가 없으므로 삭제
//            return null;
//        }
        String token = memberService.login(member.getUserId(), member.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/member/login")
    public ResponseEntity<String> login(@RequestBody MemberDTO.LoginReq request){
        String token = memberService.login(request.getUserId(), request.getPassword());
//        if (member == null) { // member가 null이 될 경우의 수가 없으므로 삭제
//            return null;
//        }
        return ResponseEntity.ok(jwtUtility.generateToken(token));
    }

    @GetMapping("/member/{userId}")
    public ResponseEntity<MemberDTO.MemberRes> getMember(@PathVariable("userId") String userId){
        Member member = memberService.findByUserId(userId);
        return ResponseEntity.ok(new MemberDTO.MemberRes(member.getUserId(),member.getNickname()));
    }

    @PutMapping("/member")
    public ResponseEntity<MemberDTO.MemberRes> changeMemberName(@RequestHeader("Authorization") String token, @RequestBody MemberDTO.MemberUpdateReq request){
        jwtUtility.validateToken(token);
        Member findMember = memberService.changeName(token, request.getNickname());
        return ResponseEntity.ok(new MemberDTO.MemberRes(findMember.getUserId(),findMember.getNickname()));
    }

    @DeleteMapping("/member")
    public ResponseEntity<Boolean> deleteMember(@RequestHeader("Authorization") String token, @RequestBody MemberDTO.DeleteReq request){
        jwtUtility.validateToken(token);
        return ResponseEntity.ok(memberService.deleteMember(request.getUserId()));
    }
}
