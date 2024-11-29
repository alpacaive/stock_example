package alpacaive.stock.repository;

import alpacaive.stock.domain.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface StockRepository extends JpaRepository<Stock, Long> {

    // Pessimistic Lock (비관적 락)
    // Spring Data JPA 에서는 lock 이라는 어노테이션을 통해서 손쉽게 Pessimistic Lock 을 구현할 수 있다.
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Stock s where s.id = :id")
    Stock findByIdWithPessimisticLock(Long id);

    // Optimistic Lock (낙관적 락)
    // Spring Data JPA 에서는 lock 이라는 어노테이션을 통해서 손쉽게 Optimistic Lock 을 구현할 수 있다.
    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from Stock s where s.id = :id")
    Stock findByIdWithOptimisticLock(Long id);

}
