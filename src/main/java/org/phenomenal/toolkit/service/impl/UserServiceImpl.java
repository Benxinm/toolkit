package org.phenomenal.toolkit.service.impl;

import org.phenomenal.toolkit.dao.UserDao;
import org.phenomenal.toolkit.entities.net.Base;
import org.phenomenal.toolkit.entities.User;
import org.phenomenal.toolkit.entities.net.UserLoginResponse;
import org.phenomenal.toolkit.oss.UserOss;
import org.phenomenal.toolkit.service.UserService;
import org.phenomenal.toolkit.utils.Auth;
import org.phenomenal.toolkit.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserOss userOss;
    @Autowired
    public UserServiceImpl(UserDao userDao, UserOss userOss) {
        this.userDao = userDao;
        this.userOss = userOss;
    }
    @Override
    public Base<UserLoginResponse> login(String username, String password) {
        User user = userDao.getUserByUsername(username);
        Base<UserLoginResponse> resp = new Base();
        if (user == null){
            resp.setStatusCode(400);
            resp.setStatusMsg("User not found");
            resp.setData(new UserLoginResponse("","",""));
            return resp;
        }
        if (user.getPassword().equals(password)){
            resp.setStatusCode(200);
            resp.setStatusMsg("Success");
            String token = Auth.genToken(user.getId().toString());
            resp.setData(new UserLoginResponse(
               token,user.getUsername(),user.getAvatar()
            ));
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

    @Override
    public Base uploadAvatar(String token, MultipartFile avatar) {
        Base resp = new Base();
        String uid = Auth.verifyToken(token);
        if (!avatar.isEmpty()){
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(avatar.getBytes());
                String objectName = new StringBuilder().append("avatar_uid_").append(uid).append(FileUtil.getImageExtension(avatar.getOriginalFilename())).toString();
                boolean isSuccess =  userOss.uploadAvatar(objectName,inputStream);
                if (isSuccess){
                    int effected = userDao.updateAvatar(Long.parseLong(uid), objectName);
                    if(effected > 0){
                        resp.setStatusCode(200);
                        resp.setStatusMsg("Success");
                    }
                }else {
                    resp.setStatusCode(402);
                    resp.setStatusMsg("OSS Exception");
                }
            }catch (IOException e) {
                resp.setStatusCode(403);
                resp.setStatusMsg("IO Exception");
                e.printStackTrace();
            }
        }
        return resp;
    }
}
