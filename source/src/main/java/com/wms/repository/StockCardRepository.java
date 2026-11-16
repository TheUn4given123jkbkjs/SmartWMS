package com.wms.repository;

import com.wms.entity.StockCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockCardRepository extends JpaRepository<StockCard, Long> {
    List<StockCard> findByProductIdOrderByTransactionDateDesc(Long productId);
    List<StockCard> findByProductIdAndTransactionDateBetweenOrderByTransactionDateAsc(
        Long productId, LocalDateTime startDate, LocalDateTime endDate
    );
}
