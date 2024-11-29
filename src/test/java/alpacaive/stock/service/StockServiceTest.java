package alpacaive.stock.service;

import alpacaive.stock.domain.Stock;
import alpacaive.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private PessimisticLockStockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    public void 재고감소() {
        stockService.decrease(1L, 1L);

        // 100 - 1 = 99
        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(99, stock.getQuantity());
    }

    @Test
    public void 동시에_100개_요청() throws InterruptedException {
        int threadCount = 100;
        // 익스큐터 서비스 사용
        // 익스큐터 서비스는 비동기로 실행하는 작업을 단순화하여 사용할 수 있게 도와조는 JAVA의 API
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // CountDownLatch 는 다른 Thread 에서 수행 중인 작업이 완료될 때까지 대기할 수 있도록 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();
        // 100 - ( 1 * 100 ) = 0
        assertEquals(0, stock.getQuantity());
        // 예상 결과가 다르다
        // 이유 : 레이스 컨디션이 일어났기 때문
        // 레이스 컨디션이란 둘 이상의 Thread 가 공유 데이터에 엑세스 할 수 있고 동시에 변경을 하려고 할때 발생하는 문제
    }

}