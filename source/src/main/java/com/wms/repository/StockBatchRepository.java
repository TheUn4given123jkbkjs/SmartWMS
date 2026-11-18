package com.wms.repository;

import com.wms.entity.StockBatch;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockBatchRepository extends JpaRepository<StockBatch, Long> {
  Optional<StockBatch> findByProductIdAndLocationIdAndLotNumber(
    Long productId,
    Long locationId,
    String lotNumber
  );
  List<StockBatch> findByProductId(Long productId);

  @Query(
    "SELECT b FROM StockBatch b WHERE b.product.id = :productId AND b.status = 'AVAILABLE' AND (b.onHandQty - b.reservedQty) > 0 AND (b.expDate IS NULL OR b.expDate >= :currentDate) ORDER BY b.expDate ASC NULLS LAST, b.createdAt ASC, b.id ASC"
  )
  List<StockBatch> findAvailableBatchesOrderByFefo(
    @Param("productId") Long productId,
    @Param("currentDate") LocalDate currentDate
  );

  @Query(
    "SELECT b FROM StockBatch b WHERE b.expDate <= :thresholdDate AND b.onHandQty > 0 ORDER BY b.expDate ASC, b.createdAt ASC, b.id ASC"
  )
  List<StockBatch> findNearExpiryBatches(
    @Param("thresholdDate") LocalDate thresholdDate
  );
}
