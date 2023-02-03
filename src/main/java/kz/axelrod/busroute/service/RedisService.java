package kz.axelrod.busroute.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveData(String key, Object data) {
        log.info("REDIS --- save data {} with key {}", data, key);
        redisTemplate.opsForValue().set(key, data);
    }

    public Object retrieveData(String key) {
        log.info("REDIS --- retrieve data with key {}", key);
        return redisTemplate.opsForValue().get(key);
    }
}
