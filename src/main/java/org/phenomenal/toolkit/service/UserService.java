package org.phenomenal.toolkit.service;

import org.phenomenal.toolkit.entities.net.Base;
import org.phenomenal.toolkit.entities.net.UserLoginResponse;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {
    Base<UserLoginResponse> login(String username, String password);
    Base register(String username,String password);
    Base uploadAvatar(String token, MultipartFile avatar);
}
