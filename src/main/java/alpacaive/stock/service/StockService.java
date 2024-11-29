package alpacaive.stock.service;

import alpacaive.stock.domain.Stock;
import alpacaive.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // synchronized 를 메서드 선언부에 붙여주면 해당 메소드는 한 개의 Thread 만 접근이 가능하게 된다.
    // @Transactional -> TransactionStockService
    public synchronized void decrease(Long id, Long quantity) {
        // Stock 조회
        // 재고를 감소시킨 뒤
        // 갱신된 값을 저장

        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }

}
