package com.wms.repository;

import com.wms.entity.PurchaseOrder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository truy xuất dữ liệu Đơn mua hàng (bảng purchase_order).
 */
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
  Optional<PurchaseOrder> findByPoNumber(String poNumber);
  boolean existsByPoNumber(String poNumber);
}
