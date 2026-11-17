package com.wms.entity;

import com.wms.util.ChangeType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Thực thể Sổ Thẻ kho điện tử - Lịch sử biến động tồn kho bất biến (Bảng stock_card).
 */
@Entity
@Table(name = "stock_card")
public class StockCard {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "batch_id", nullable = false)
  private StockBatch stockBatch;

  @Column(name = "transaction_date", nullable = false)
  private LocalDateTime transactionDate = LocalDateTime.now();

  @Column(name = "reference_code", nullable = false, length = 50)
  private String referenceCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "change_type", nullable = false, length = 20)
  private ChangeType changeType;

  @Column(name = "qty_changed", nullable = false, precision = 12, scale = 2)
  private BigDecimal qtyChanged;

  @Column(name = "after_changed", nullable = false, precision = 12, scale = 2)
  private BigDecimal afterChanged;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @ManyToOne
  @JoinColumn(name = "performed_by")
  private User performedBy;

  public User getPerformedBy() {
    return performedBy;
  }

  public void setPerformedBy(User value) {
    performedBy = value;
  }

  @ManyToOne
  @JoinColumn(name = "approved_by")
  private User approvedBy;

  public User getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(User value) {
    approvedBy = value;
  }

  @Column(length = 500)
  private String reason;

  public String getReason() {
    return reason;
  }

  public void setReason(String value) {
    reason = value;
  }

  public StockCard() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public StockBatch getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(StockBatch stockBatch) {
    this.stockBatch = stockBatch;
  }

  public LocalDateTime getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(LocalDateTime transactionDate) {
    this.transactionDate = transactionDate;
  }

  public String getReferenceCode() {
    return referenceCode;
  }

  public void setReferenceCode(String referenceCode) {
    this.referenceCode = referenceCode;
  }

  public ChangeType getChangeType() {
    return changeType;
  }

  public void setChangeType(ChangeType changeType) {
    this.changeType = changeType;
  }

  public BigDecimal getQtyChanged() {
    return qtyChanged;
  }

  public void setQtyChanged(BigDecimal qtyChanged) {
    this.qtyChanged = qtyChanged;
  }

  public BigDecimal getAfterChanged() {
    return afterChanged;
  }

  public void setAfterChanged(BigDecimal afterChanged) {
    this.afterChanged = afterChanged;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
