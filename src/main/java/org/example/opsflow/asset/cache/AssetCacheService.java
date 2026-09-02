package org.example.opsflow.asset.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.opsflow.asset.dto.AssetResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetCacheService {
    private static final String KEY_PREFIX = "opsflow:cache:asset:detail:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private String buildKey(Long assetId){
        return KEY_PREFIX + assetId;
    }

    public AssetResponse get(Long assetId){
        try {
            String json = redisTemplate.opsForValue().get(buildKey(assetId));
            if(json == null){
                return null;
            }
            return objectMapper.readValue(json,AssetResponse.class);
        }catch (DataAccessException | JsonProcessingException e){
            log.warn("读取资产详情缓存失败,assetId:{}",assetId,e);
            return null;
        }
    }

    public void save(Long assetId, AssetResponse response){
        try {
            String json = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(buildKey(assetId), json, CACHE_TTL);
        }catch (JsonProcessingException | DataAccessException e){
            log.warn("写入资产详情缓存失败，assetId={}", assetId, e);
        }
    }

    public void remove(Long assetId){
        try {
            redisTemplate.delete(buildKey(assetId));
        }catch (DataAccessException e){
            log.warn("删除资产详情缓存失败，assetId={}", assetId, e);
        }

    }
}
