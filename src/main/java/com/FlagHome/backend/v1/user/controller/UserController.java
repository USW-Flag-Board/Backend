package com.FlagHome.backend.v1.user.controller;

import com.FlagHome.backend.v1.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";  //리턴값 뭐라고 하죠...?
    }


}