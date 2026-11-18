package com.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
  uniqueConstraints = @UniqueConstraint(
    columnNames = { "purchase_order_id", "product_id" }
  )
)
public class PurchaseOrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "purchase_order_id", nullable = false)
  private PurchaseOrder purchaseOrder;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal orderedQty;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal receivedQty = BigDecimal.ZERO;

  public Long getId() {
    return id;
  }

  public PurchaseOrder getPurchaseOrder() {
    return purchaseOrder;
  }

  public void setPurchaseOrder(PurchaseOrder v) {
    purchaseOrder = v;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product v) {
    product = v;
  }

  public BigDecimal getOrderedQty() {
    return orderedQty;
  }

  public void setOrderedQty(BigDecimal v) {
    orderedQty = v;
  }

  public BigDecimal getReceivedQty() {
    return receivedQty;
  }

  public void setReceivedQty(BigDecimal v) {
    receivedQty = v;
  }
}
