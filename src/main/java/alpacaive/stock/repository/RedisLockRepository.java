package alpacaive.stock.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

// Redis 의 명령어를 이용하기 위해 만듬
@Component
public class RedisLockRepository {

    // Redis 의 명령어를 실행할 수 있어야 되기 때문에 Redis 템플릿을 변수로 추가
    private RedisTemplate<String, String> redisTemplate;

    public RedisLockRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Lock 메서드 구현
    // 키에 사용할 변수를 받아야 되기 때문에 매개 변수에 키라는 변수를 추가
    public Boolean lock(Long key) {
        return redisTemplate
                .opsForValue()
                .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_000));
    }

    private String generateKey(Long key) {
        return key.toString();
    }

    // UnLock 메서드 구현
    public Boolean unlock(Long key) {
        return redisTemplate.delete(generateKey(key));
    }

}
