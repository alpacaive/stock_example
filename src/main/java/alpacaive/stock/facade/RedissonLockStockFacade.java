package alpacaive.stock.facade;

import alpacaive.stock.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

// Redisson 같은 경우에는 Lock 관련된 클래스들을 라이브러리에서 제공을 해주므로 별도의 Repository 를 작성하지 않아도 됨.
// 로직 실행 전후로 Lock 획득/해제는 해줘야 하므로 클래스를 하나 생성
@Component
public class RedissonLockStockFacade {

    // Lock 획득에 사용할 Redisson client 를 필드로 추가
    private RedissonClient redissonClient;

    private StockService stockService;

    public RedissonLockStockFacade(RedissonClient redissonClient, StockService stockService) {
        this.redissonClient = redissonClient;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) {
        // redissonClient 활용해 Lock 객체 가져옴
        RLock lock = redissonClient.getLock(id.toString());

        try {
            // 몇 초 동안 Lock 획득을 시도할 것인지, 몇 초 동안 점유할 것인지 설정
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("Lock 획득 실패");
                return;
            }

            stockService.decrease(id, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}
