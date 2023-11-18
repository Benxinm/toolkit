package org.phenomenal.toolkit.service;

import org.phenomenal.toolkit.entities.net.Base;
import org.phenomenal.toolkit.entities.User;
import org.phenomenal.toolkit.entities.net.UserLoginResponse;

public interface UserService {
    Base<UserLoginResponse> login(String username, String password);
    Base register(String username,String password);
}
