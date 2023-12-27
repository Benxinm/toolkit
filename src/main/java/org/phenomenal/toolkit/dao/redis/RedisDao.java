package org.phenomenal.toolkit.dao.redis;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDao {
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    public void setMap(String mapKey,Map<String,String> map,Long expireTime){
        redisTemplate.opsForHash().putAll(mapKey,map);
        setExpireTime(mapKey,expireTime);
    }

    public String getMapValue(String mapKey,String hash){
        return (String)redisTemplate.opsForHash().get(mapKey,hash);
    }

    public void setExpireTime(String key,Long expireTime){
        redisTemplate.expire(key,expireTime, TimeUnit.MILLISECONDS);
    }

    public void set(String key,String value,Long expireTime){
        redisTemplate.opsForValue().set(key,value,expireTime);
    }
    public boolean exist(String key){
        return redisTemplate.hasKey(key);
    }
}
