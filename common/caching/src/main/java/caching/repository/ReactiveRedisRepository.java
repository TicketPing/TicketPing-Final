package caching.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ReactiveRedisRepository {

    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;
    private final ObjectMapper objectMapper;

    public <T> Mono<T> getValueAsClass(String key, Class<T> clazz) {
        return reactiveRedisTemplate.opsForValue()
                .get(key)
                .map(value -> objectMapper.convertValue(value, clazz));
    }

    public Mono<Long> getSortedSetSize(String key) {
        return reactiveRedisTemplate.opsForZSet()
                .size(key);
    }

    public Mono<Boolean> hasKey(String key) {
        return reactiveRedisTemplate.hasKey(key);
    }

}