package alpacaive.stock.service;

import alpacaive.stock.domain.Stock;
import alpacaive.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // synchronized 를 메서드 선언부에 붙여주면 해당 메소드는 한 개의 Thread 만 접근이 가능하게 된다.
    @Transactional(propagation = Propagation.REQUIRES_NEW) // -> TransactionStockService
    // Named Lock : 부모의 트랜잭션과 별도로 실행되어야 되기 때문에 propagation 을 변경 (synchronized 삭제, @Transactional(propagation = Propagation.REQUIRES_NEW) 추가)
    public void decrease(Long id, Long quantity) {
        // Stock 조회
        // 재고를 감소시킨 뒤
        // 갱신된 값을 저장- 

        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }

}
