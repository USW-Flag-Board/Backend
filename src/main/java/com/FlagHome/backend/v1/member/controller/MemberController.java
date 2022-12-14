package com.FlagHome.backend.v1.member.controller;

import com.FlagHome.backend.v1.member.service.MemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService userService;

    public MemberController(MemberService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";  //리턴값 뭐라고 하죠...?
    }


}