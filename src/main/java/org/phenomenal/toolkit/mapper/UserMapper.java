package org.phenomenal.toolkit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.phenomenal.toolkit.entities.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * from user where username = #{username}")
    User getUserByUsername(String username);
    @Update("UPDATE `user` set avatar = #{url} where id = #{uid}")
    int  updateAvatar(long uid,String url);
}
