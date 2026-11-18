package com.wms.config;

import com.wms.entity.*;
import com.wms.repository.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(10)
public class CoreDataInitializer implements CommandLineRunner {

  @PersistenceContext
  private EntityManager em;

  private final ProductRepository products;
  private final PurchaseOrderRepository orders;

  public CoreDataInitializer(ProductRepository p, PurchaseOrderRepository o) {
    products = p;
    orders = o;
  }

  @Transactional
  public void run(String... args) {
    if (em.find(WarehouseMutex.class, 1L) == null) em.persist(
      new WarehouseMutex(1L)
    );
    if (orders.count() == 0) for (int n = 1; n <= 3; n++) {
      PurchaseOrder po = new PurchaseOrder();
      po.setPoNumber("PO-DEMO-00" + n);
      po.setSupplierName("Nhà cung cấp Yamaha — PO demo " + n);
      for (Product product : products.findAll()) {
        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setPurchaseOrder(po);
        item.setProduct(product);
        item.setOrderedQty(new BigDecimal("500"));
        po.getItems().add(item);
      }
      orders.save(po);
    }
  }
}
