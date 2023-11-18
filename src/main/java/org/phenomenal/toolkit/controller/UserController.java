package org.phenomenal.toolkit.controller;

import lombok.extern.slf4j.Slf4j;
import org.phenomenal.toolkit.entities.net.Base;
import org.phenomenal.toolkit.entities.net.UserLoginResponse;
import org.phenomenal.toolkit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Base<UserLoginResponse> login(@RequestParam("username") String username,
                                         @RequestParam("password") String password) {
        Base<UserLoginResponse> resp = userService.login(username, password);
        return resp;
    }
    @PostMapping("/register")
    public Base register(@RequestParam("username") String username,
                         @RequestParam("password") String password){
        Base resp = userService.register(username, password);
        return resp;
    }
}
