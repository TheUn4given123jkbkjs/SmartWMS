package com.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Thực thể Mặt hàng lưu kho / SKU (Bảng product trong CSDL 3NF).
 */
@Entity
@Table(
  name = "product",
  uniqueConstraints = {
    @UniqueConstraint(name = "uk_product_sku", columnNames = { "sku_code" }),
  }
)
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Column(name = "sku_code", nullable = false, length = 50)
  private String skuCode;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "uom", nullable = false, length = 50)
  private String uom;

  @Column(name = "min_stock", nullable = false, precision = 12, scale = 2)
  private BigDecimal minStock = BigDecimal.ZERO;

  @Column(name = "max_stock", nullable = false, precision = 12, scale = 2)
  private BigDecimal maxStock;

  @Column(name = "manage_by_lot", nullable = false)
  private Boolean manageByLot = true;

  @Column(name = "active", nullable = false)
  private Boolean active = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public Product() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public String getSkuCode() {
    return skuCode;
  }

  public void setSkuCode(String skuCode) {
    this.skuCode = skuCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUom() {
    return uom;
  }

  public void setUom(String uom) {
    this.uom = uom;
  }

  public BigDecimal getMinStock() {
    return minStock;
  }

  public void setMinStock(BigDecimal minStock) {
    this.minStock = minStock;
  }

  public BigDecimal getMaxStock() {
    return maxStock;
  }

  public void setMaxStock(BigDecimal maxStock) {
    this.maxStock = maxStock;
  }

  public Boolean getManageByLot() {
    return manageByLot;
  }

  public void setManageByLot(Boolean manageByLot) {
    this.manageByLot = manageByLot;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
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
