package org.phenomenal.toolkit.service.impl;

import org.phenomenal.toolkit.dao.HistoryDao;
import org.phenomenal.toolkit.dao.UserDao;
import org.phenomenal.toolkit.entities.History;
import org.phenomenal.toolkit.entities.ToolType;
import org.phenomenal.toolkit.entities.net.Base;
import org.phenomenal.toolkit.entities.User;
import org.phenomenal.toolkit.entities.net.CleanBase;
import org.phenomenal.toolkit.entities.net.HistoryResponse;
import org.phenomenal.toolkit.entities.net.UserLoginResponse;
import org.phenomenal.toolkit.oss.UserOss;
import org.phenomenal.toolkit.service.UserService;
import org.phenomenal.toolkit.util.Auth;
import org.phenomenal.toolkit.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserOss userOss;
    private final UserDao userDao;
    private final HistoryDao historyDao;
    @Autowired
    public UserServiceImpl(UserDao userDao, UserOss userOss,HistoryDao historyDao) {
        this.userDao = userDao;
        this.userOss = userOss;
        this.historyDao = historyDao;
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
    public CleanBase register(String username, String password) {
        CleanBase resp = new CleanBase();
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
    public CleanBase uploadAvatar(String token, MultipartFile avatar) {
        CleanBase resp = new CleanBase();
        String uid = Auth.verifyToken(token);
        if (uid == null){
            resp.setStatusCode(404);
            resp.setStatusMsg("Expired Token");
            return resp;
        }
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

    @Override
    public CleanBase putHistory(String token, int code, String content) {
        CleanBase resp = new CleanBase();
        String uidString = Auth.verifyToken(token);
        if (uidString == null){
            resp.setStatusCode(404);
            resp.setStatusMsg("Expired Token");
            return resp;
        }
        if (content.isEmpty() || content.isBlank()){
            resp.setStatusCode(407);
            resp.setStatusMsg("Empty Content");
            return resp;
        }
        ToolType type = ToolType.valueOf(code);
        if (type == null){
            resp.setStatusCode(406);
            resp.setStatusMsg("Invalid Type");
            return resp;
        }
        long uid = Long.parseLong(uidString);
        History history = new History(uid, type, content);

        int effected = historyDao.insertHistory(history);
        if (effected > 0){
            resp.setStatusCode(200);
            resp.setStatusMsg("Success");
        }else {
            resp.setStatusCode(405);
            resp.setStatusMsg("Internal Error");
        }
        return resp;
    }

    @Override
    public Base<HistoryResponse> getHistory(long userId,String token) {
        Base<HistoryResponse> resp = new Base<>();
        String uidString = Auth.verifyToken(token);
        if (uidString == null){
            resp.setStatusCode(404);
            resp.setStatusMsg("Expired Token");
            return resp;
        }
        long uid = Long.parseLong(uidString);
        if (userId == uid){
            List<History> histories = historyDao.getUserHistory(uid);
            HistoryResponse historyResponse = new HistoryResponse(histories);
            resp.setData(historyResponse);
            resp.setStatusCode(200);
            resp.setStatusMsg("Success");
        }
        return resp;
    }
}
