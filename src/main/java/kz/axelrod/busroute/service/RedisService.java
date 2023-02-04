package kz.axelrod.busroute.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Slf4j
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX_DIRECT_ROUTE = "route:direct:";

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getKeyForDirectRoute(String fromId, String toId) {
        return MessageFormat.format("{0}{1}:{2}", KEY_PREFIX_DIRECT_ROUTE, fromId, toId);
    }

    public void saveData(String key, Object data) {
        log.info("REDIS --- save data {} with key {}", data, key);
        redisTemplate.opsForValue().set(key, data);
    }

    public Object getData(String key) {
        log.info("REDIS --- retrieve data with key {}", key);
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        log.info("REDIS --- delete data with key {}", key);
        redisTemplate.delete(key);
    }

    public Boolean hasKeyOfData(String key) {
        log.info("REDIS --- has key of data {}", key);
        return redisTemplate.hasKey(key);
    }
}
