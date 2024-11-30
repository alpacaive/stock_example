package alpacaive.stock.facade;

import alpacaive.stock.repository.RedisLockRepository;
import alpacaive.stock.service.StockService;
import org.springframework.stereotype.Component;

// 레디스를 활용하는 방식도 로직 실행 전후로 Lock 획득/해제를 수행해줘야 되기 때문에 facade 클래스 생성
@Component
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(id)) {
            // Lock 획득 실패시 Thread.sleep 을 사용해서 100ms의 텀을 두고 Lock 획득을 재시도하도록 한다.
            // -> 이렇게 해야 Redis 에 갈 수 있는 부화를 줄여준다.
            Thread.sleep(100);
        }
        // Lock 획득에 성공 시, 재고 감소
        try {
            stockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id);
        }
    }

}
