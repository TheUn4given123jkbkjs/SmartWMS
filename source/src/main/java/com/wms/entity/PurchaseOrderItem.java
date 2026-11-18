package com.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Thực thể Chi tiết Đơn mua hàng (PO Detail) - Bảng purchase_order_item.
 * Quản lý số lượng đặt mua và số lượng đã giao lũy kế của từng mặt hàng SKU.
 */
@Entity
@Table(
  name = "purchase_order_item",
  uniqueConstraints = @UniqueConstraint(
    name = "uk_po_item_product",
    columnNames = { "purchase_order_id", "product_id" }
  )
)
public class PurchaseOrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "purchase_order_id", nullable = false)
  private PurchaseOrder purchaseOrder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(name = "ordered_qty", nullable = false, precision = 12, scale = 2)
  private BigDecimal orderedQty;

  @Column(name = "received_qty", nullable = false, precision = 12, scale = 2)
  private BigDecimal receivedQty = BigDecimal.ZERO;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at")
  private LocalDateTime updatedAt = LocalDateTime.now();

  public PurchaseOrderItem() {}

  public PurchaseOrderItem(PurchaseOrder purchaseOrder, Product product, BigDecimal orderedQty) {
    this.purchaseOrder = purchaseOrder;
    this.product = product;
    this.orderedQty = orderedQty;
    this.receivedQty = BigDecimal.ZERO;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PurchaseOrder getPurchaseOrder() {
    return purchaseOrder;
  }

  public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
    this.purchaseOrder = purchaseOrder;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public BigDecimal getOrderedQty() {
    return orderedQty;
  }

  public void setOrderedQty(BigDecimal orderedQty) {
    this.orderedQty = orderedQty;
  }

  public BigDecimal getReceivedQty() {
    return receivedQty;
  }

  public void setReceivedQty(BigDecimal receivedQty) {
    this.receivedQty = receivedQty;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
