package org.phenomenal.toolkit.dao;

import org.phenomenal.toolkit.entities.User;
import org.phenomenal.toolkit.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDao {
    private final UserMapper userMapper;

    @Autowired
    public UserDao(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserByUsername(String username){
        User user = userMapper.getUserByUsername(username);
        return user;
    }

    public int insertUser(User user){
        int res = userMapper.insert(user);
        return res;
    }
    public int updateAvatar(long uid,String url){
        int res = userMapper.updateAvatar(uid,url);
        return res;
    }
}
