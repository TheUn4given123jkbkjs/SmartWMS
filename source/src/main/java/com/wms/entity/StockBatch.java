package com.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Thực thể Lô hàng tồn kho (Bảng stock_batch - Thực thể trung tâm phân rã N:N).
 */
@Entity
@Table(
  name = "stock_batch",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_batch_product_loc_lot",
      columnNames = { "product_id", "location_id", "lot_number" }
    ),
  }
)
public class StockBatch {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id", nullable = false)
  private WarehouseLocation location;

  @Column(name = "lot_number", nullable = false, length = 50)
  private String lotNumber;

  @Column(name = "exp_date")
  private LocalDate expDate;

  @Column(name = "on_hand_qty", nullable = false, precision = 12, scale = 2)
  private BigDecimal onHandQty = BigDecimal.ZERO;

  @Column(name = "reserved_qty", nullable = false, precision = 12, scale = 2)
  private BigDecimal reservedQty = BigDecimal.ZERO;

  @Column(name = "status", nullable = false, length = 20)
  private String status = "AVAILABLE";

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public StockBatch() {}

  /**
   * Tồn khả dụng (Available Qty) tính toán động = onHandQty - reservedQty
   */
  public BigDecimal getAvailableQty() {
    return onHandQty.subtract(reservedQty);
  }

  public boolean isEligible() {
    return (
      "AVAILABLE".equals(status) &&
      (expDate == null || !expDate.isBefore(LocalDate.now())) &&
      (location.getZone() == com.wms.util.ZoneType.RAW ||
        location.getZone() == com.wms.util.ZoneType.FG)
    );
  }

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

  public WarehouseLocation getLocation() {
    return location;
  }

  public void setLocation(WarehouseLocation location) {
    this.location = location;
  }

  public String getLotNumber() {
    return lotNumber;
  }

  public void setLotNumber(String lotNumber) {
    this.lotNumber = lotNumber;
  }

  public LocalDate getExpDate() {
    return expDate;
  }

  public void setExpDate(LocalDate expDate) {
    this.expDate = expDate;
  }

  public BigDecimal getOnHandQty() {
    return onHandQty;
  }

  public void setOnHandQty(BigDecimal onHandQty) {
    this.onHandQty = onHandQty;
  }

  public BigDecimal getReservedQty() {
    return reservedQty;
  }

  public void setReservedQty(BigDecimal reservedQty) {
    this.reservedQty = reservedQty;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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
