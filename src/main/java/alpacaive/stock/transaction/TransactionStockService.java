package alpacaive.stock.transaction;

import alpacaive.stock.service.StockService;

// @Transaction 동작방식
public class TransactionStockService {

    // StockService 를 필드로 가지는 클래스를 새로 만들어서 실행
    private StockService stockService;

    public TransactionStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) {
        startTransaction();

        stockService.decrease(id, quantity);
        // 여기서 문제 발생
        // 트랜잭션 종료 시점에 데이터베이스에 업데이트 하는데,
        // decrease() 메소드가 완료되고 데이터베이스가 업데이트 되기 전에 다른 Thread 가 decrease() 메소드 호출 가능
        // = 이전과 동일한 문제가 발생
        // 해결 방법 -> Transactional 애노테이션 제거
        endTransaction();

    }

    private void startTransaction() {
        System.out.println("Transaction Start");
    }

    private void endTransaction() {
        System.out.println("Commit");
    }

}
