package org.phenomenal.toolkit.service.impl;

import org.phenomenal.toolkit.dao.UserDao;
import org.phenomenal.toolkit.entities.net.Base;
import org.phenomenal.toolkit.entities.User;
import org.phenomenal.toolkit.entities.net.UserLoginResponse;
import org.phenomenal.toolkit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Base<UserLoginResponse> login(String username, String password) {
        User user = userDao.getUserByUsername(username);
        Base<UserLoginResponse> resp = new Base();
        if (user == null){
            resp.setStatusCode(400);
            resp.setStatusMsg("User not found");
            resp.setData(new UserLoginResponse(""));
            return resp;
        }
        if (user.getPassword().equals(password)){
            resp.setStatusCode(200);
            resp.setStatusMsg("Success");
            resp.setData(new UserLoginResponse("123456789"));
        }else {
            resp.setStatusCode(401);
            resp.setStatusMsg("Wrong password");
        }
        return resp;
    }

    @Override
    public Base register(String username, String password) {
        Base resp = new Base();
        int res = userDao.insertUser(new User(username, password));
        if (res == 1){
            resp.setStatusCode(200);
            resp.setStatusMsg("Success");
        }else {
            resp.setStatusCode(402);
            resp.setStatusMsg("User exist");
        }
        return resp;
    }
}
