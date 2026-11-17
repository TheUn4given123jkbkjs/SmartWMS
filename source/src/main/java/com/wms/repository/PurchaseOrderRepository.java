package com.wms.repository;

import com.wms.entity.PurchaseOrder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository
  extends JpaRepository<PurchaseOrder, Long> {
  Optional<PurchaseOrder> findByPoNumber(String number);
}
