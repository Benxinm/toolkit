package org.phenomenal.toolkit.service;

import org.phenomenal.toolkit.entities.net.*;
import org.phenomenal.toolkit.entities.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Base<UserLoginResponse> login(String username, String password);
    CleanBase register(String username, String password);
    Base<AvatarResponse> uploadAvatar(String token, MultipartFile avatar);
    CleanBase putHistory(String token,int code,String content);
    Base<HistoryResponse> getHistory(long uid, String token);
}

