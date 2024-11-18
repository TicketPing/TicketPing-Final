package caching.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setValues(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    public void setValueWithTTL(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public <T> T getValueAsClass(String key, Class<T> clazz) {
        return objectMapper.convertValue(redisTemplate.opsForValue().get(key), clazz);
    }

    public <T> List<T> getValuesAsClass(List<String> keys, Class<T> clazz) {
        return redisTemplate.opsForValue().multiGet(keys)
                .stream().map(v -> objectMapper.convertValue(v, clazz)).toList();
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public void increment(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    public void decrement(String key) {
        redisTemplate.opsForValue().decrement(key);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Long getSortedSetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

}
