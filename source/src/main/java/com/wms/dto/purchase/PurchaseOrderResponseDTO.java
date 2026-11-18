package com.wms.dto.purchase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO trả về thông tin đơn mua hàng PO kèm danh sách chi tiết các dòng hàng.
 */
public class PurchaseOrderResponseDTO {

  private Long id;
  private String poNumber;
  private String supplierName;
  private String status;
  private LocalDateTime createdAt;
  private List<PurchaseOrderItemDTO> items = new ArrayList<>();

  public PurchaseOrderResponseDTO() {}

  public PurchaseOrderResponseDTO(
    Long id,
    String poNumber,
    String supplierName,
    String status,
    LocalDateTime createdAt,
    List<PurchaseOrderItemDTO> items
  ) {
    this.id = id;
    this.poNumber = poNumber;
    this.supplierName = supplierName;
    this.status = status;
    this.createdAt = createdAt;
    this.items = items;
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public List<PurchaseOrderItemDTO> getItems() {
    return items;
  }

  public void setItems(List<PurchaseOrderItemDTO> items) {
    this.items = items;
  }
}
