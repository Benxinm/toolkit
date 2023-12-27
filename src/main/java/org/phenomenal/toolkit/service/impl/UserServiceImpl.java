package org.phenomenal.toolkit.service.impl;

import org.phenomenal.toolkit.dao.HistoryDao;
import org.phenomenal.toolkit.dao.UserDao;
import org.phenomenal.toolkit.dao.redis.RedisDao;
import org.phenomenal.toolkit.entities.History;
import org.phenomenal.toolkit.entities.ToolType;
import org.phenomenal.toolkit.entities.net.*;
import org.phenomenal.toolkit.entities.User;
import org.phenomenal.toolkit.oss.UserOss;
import org.phenomenal.toolkit.service.UserService;
import org.phenomenal.toolkit.util.Auth;
import org.phenomenal.toolkit.util.Constant;
import org.phenomenal.toolkit.util.FileUtil;
import org.phenomenal.toolkit.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserOss userOss;
    private final UserDao userDao;
    private final HistoryDao historyDao;
    private final RedisDao redisDao;
    @Autowired
    public UserServiceImpl(UserDao userDao, UserOss userOss, HistoryDao historyDao, RedisDao redisDao) {
        this.userDao = userDao;
        this.userOss = userOss;
        this.historyDao = historyDao;
        this.redisDao = redisDao;
    }
    @Override
    public Base<UserLoginResponse> login(String username, String password) {
        Base<UserLoginResponse> resp = new Base();
        UserLoginResponse data = new UserLoginResponse();
        if (redisDao.exist(username)){
            String psd = redisDao.getMapValue(username,"psd");
            if (psd.equals(password)){
                Status.set(resp,200);
                String token = redisDao.getMapValue(username, "token");
                String avatar = redisDao.getMapValue(username, "avatar");
                data.setToken(token);
                data.setUsername(username);
                data.setAvatar(avatar);
                resp.setData(data);
                return resp;
            }
        }
        User user = userDao.getUserByUsername(username);
        if (user == null){
            Status.set(resp,400);
            resp.setData(data);
            return resp;
        }
        if (user.getPassword().equals(password)){
            Status.set(resp,200);
            String token = Auth.genToken(user.getId().toString());
            HashMap<String, String> map = new HashMap<>();
            map.put("token",token);
            map.put("psd",password);
            map.put("avatar",user.getAvatar());
            redisDao.setMap(username,map,Constant.EXPIRE_TIME);
            data.setToken(token);
            data.setUsername(username);
            data.setAvatar(user.getAvatar());
        }else {
            Status.set(resp,401);
        }
        resp.setData(data);
        return resp;
    }

    @Override
    public CleanBase register(String username, String password) {
        CleanBase resp = new CleanBase();
        User user = userDao.getUserByUsername(username);
        if (user != null){
            resp.setStatusCode(402);
            resp.setStatusMsg("User exist");
            return resp;
        }
        int res = userDao.insertUser(new User(username, password));
        if (res == 1){
            resp.setStatusCode(200);
            resp.setStatusMsg("Success");
        }
        return resp;
    }

    @Override
    public Base<AvatarResponse> uploadAvatar(String token, MultipartFile avatar) {
        Base<AvatarResponse> resp = new Base();
        String uid = Auth.verifyToken(token);
        if (uid == null){
            resp.setStatusCode(404);
            resp.setStatusMsg("Expired Token");
            return resp;
        }
        if (!avatar.isEmpty()){
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(avatar.getBytes());
                String avatarName = new StringBuilder().append("avatar_uid_").append(uid).append(FileUtil.getImageExtension(avatar.getOriginalFilename())).toString();
                boolean isSuccess =  userOss.uploadAvatar(avatarName,inputStream);
                if (isSuccess){
                    int effected = userDao.updateAvatar(Long.parseLong(uid), avatarName);
                    if(effected > 0){
                        resp.setStatusCode(200);
                        resp.setStatusMsg("Success");
                        resp.setData(new AvatarResponse(avatarName));
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
