package org.phenomenal.toolkit.controller;

import lombok.extern.slf4j.Slf4j;
import org.phenomenal.toolkit.entities.net.*;
import org.phenomenal.toolkit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
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
    public CleanBase register(@RequestParam("username") String username,
                              @RequestParam("password") String password){
        CleanBase resp = userService.register(username, password);
        return resp;
    }
    @PostMapping("/upload_avatar")
    public Base<AvatarResponse> uploadAvatar(@RequestParam("token") String token,
                                             @RequestParam("avatar") MultipartFile avatar){
        Base<AvatarResponse> resp = userService.uploadAvatar(token,avatar);
        return resp;
    }
    @PostMapping("/put_history")
    public CleanBase putHistory(@RequestParam("token") String token,
                                @RequestParam("type") int type,
                                @RequestParam("content") String content){
        CleanBase resp = userService.putHistory(token, type, content);
        return resp;
    }
    @GetMapping("get_history")
    public Base<HistoryResponse> getHistory(@RequestParam("uid") long uid,
                                            @RequestParam("token") String token){
        Base<HistoryResponse> resp = userService.getHistory(uid,token);
        return resp;
    }
}
