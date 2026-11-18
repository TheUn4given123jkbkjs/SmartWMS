package com.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Thực thể Đơn mua hàng (PO Header) - Bảng purchase_order.
 * Đóng vai trò dữ liệu tham chiếu đối soát số lượng và chặn nhận thừa khi nhập kho (BR-INB-01).
 */
@Entity
@Table(
  name = "purchase_order",
  uniqueConstraints = {
    @UniqueConstraint(name = "uk_po_number", columnNames = { "po_number" })
  }
)
public class PurchaseOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "po_number", nullable = false, length = 50)
  private String poNumber;

  @Column(name = "supplier_name", nullable = false)
  private String supplierName;

  @Column(name = "status", nullable = false, length = 30)
  private String status = "APPROVED";

  @OneToMany(
    mappedBy = "purchaseOrder",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<PurchaseOrderItem> items = new ArrayList<>();

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at")
  private LocalDateTime updatedAt = LocalDateTime.now();

  public PurchaseOrder() {}

  public PurchaseOrder(String poNumber, String supplierName) {
    this.poNumber = poNumber;
    this.supplierName = supplierName;
    this.status = "APPROVED";
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPoNumber() {
    return poNumber;
  }

  public void setPoNumber(String poNumber) {
    this.poNumber = poNumber;
  }

  public String getSupplierName() {
    return supplierName;
  }

  public void setSupplierName(String supplierName) {
    this.supplierName = supplierName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<PurchaseOrderItem> getItems() {
    return items;
  }

  public void setItems(List<PurchaseOrderItem> items) {
    this.items = items;
  }

  public void addItem(PurchaseOrderItem item) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(item);
    item.setPurchaseOrder(this);
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
