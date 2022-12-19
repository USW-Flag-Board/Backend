package com.FlagHome.backend.v1.member.controller;

import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.member.dto.WithdrawRequest;
import com.FlagHome.backend.v1.member.service.MemberService;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(WithdrawRequest withdrawRequest){
        memberService.withdrawMember(withdrawRequest);
        return ResponseEntity.ok().build();
    }
}