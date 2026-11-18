package com.wms.service;

import com.wms.entity.Product;
import com.wms.entity.StockBatch;
import com.wms.entity.User;
import com.wms.entity.WarehouseLocation;
import com.wms.util.UserRole;
import java.math.BigDecimal;

/**
 * Interface định nghĩa các thao tác phụ trợ nguyên tử trong nội bộ kho (Lock, Actor, Audit, Thẻ kho).
 */
public interface WarehouseOperations {

  void lock();

  User actor(Long id, UserRole role);

  void checker(User maker, User checker);

  BigDecimal qty(BigDecimal v, boolean positive);

  String code(String prefix);

  BigDecimal onHand(Product p);

  void location(WarehouseLocation location);

  void card(
    StockBatch batch,
    BigDecimal change,
    String reference,
    User actor,
    User checker,
    String reason
  );

  void audit(String reference, String action, User actor);
}
