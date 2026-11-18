package com.wms.dto.receipt;

import com.wms.util.ReceiptStatus;
import java.time.LocalDateTime;
import java.util.List;

public class ReceiptNoteResponseDTO {

  private Long makerId;

  public Long getMakerId() {
    return makerId;
  }

  public void setMakerId(Long id) {
    makerId = id;
  }

  private Long id;
  private String receiptCode;
  private String poNumber;
  private String supplierName;
  private String makerName;
  private String checkerName;
  private ReceiptStatus status;
  private String note;
  private LocalDateTime createdAt;
  private LocalDateTime approvedAt;
  private List<ReceiptItemDTO> items;

  public ReceiptNoteResponseDTO() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getReceiptCode() {
    return receiptCode;
  }

  public void setReceiptCode(String receiptCode) {
    this.receiptCode = receiptCode;
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

  public String getMakerName() {
    return makerName;
  }

  public void setMakerName(String makerName) {
    this.makerName = makerName;
  }

  public String getCheckerName() {
    return checkerName;
  }

  public void setCheckerName(String checkerName) {
    this.checkerName = checkerName;
  }

  public ReceiptStatus getStatus() {
    return status;
  }

  public void setStatus(ReceiptStatus status) {
    this.status = status;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getApprovedAt() {
    return approvedAt;
  }

  public void setApprovedAt(LocalDateTime approvedAt) {
    this.approvedAt = approvedAt;
  }

  public List<ReceiptItemDTO> getItems() {
    return items;
  }

  public void setItems(List<ReceiptItemDTO> items) {
    this.items = items;
  }
}
