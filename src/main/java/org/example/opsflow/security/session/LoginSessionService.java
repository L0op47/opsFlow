package org.example.opsflow.security.session;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LoginSessionService {
    private static final String KEY_PREFIX = "opsflow:auth:login:";
    private final StringRedisTemplate redisTemplate;

    @Value("${jwt.expiration}")
    private long expiration;

    private String buildKey(String username){
        return KEY_PREFIX + username;
    }

    public void save(String username, String token){
        redisTemplate.opsForValue().set(buildKey(username),token,expiration,TimeUnit.MILLISECONDS);
    }

    public boolean isValid(String username,String token){
        String savedToken = redisTemplate.opsForValue().get(buildKey(username));
        return savedToken!= null && savedToken.equals(token);
    }

    public void remove(String username){
        redisTemplate.delete(buildKey(username));
    }


}
