package alpacaive.stock.repository;

import alpacaive.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
