package org.phenomenal.toolkit.service;

import org.phenomenal.toolkit.entities.net.Base;
import org.phenomenal.toolkit.entities.User;
import org.phenomenal.toolkit.entities.net.CleanBase;
import org.phenomenal.toolkit.entities.net.UserLoginResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Base<UserLoginResponse> login(String username, String password);
    CleanBase register(String username, String password);
    CleanBase uploadAvatar(String token, MultipartFile avatar);
}
