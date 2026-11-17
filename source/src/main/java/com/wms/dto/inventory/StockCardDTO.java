package com.wms.dto.inventory;

import com.wms.util.ChangeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StockCardDTO {

  private Long id;
  private LocalDateTime transactionTime;
  private ChangeType changeType;
  private String referenceCode;
  private String skuCode;
  private String productName;
  private String batchCode;
  private String locationCode;
  private BigDecimal beforeQty;
  private BigDecimal changeQty;
  private BigDecimal afterQty;
  private String performedByName;
  private String reason;

  private String approvedByName;

  public String getApprovedByName() {
    return approvedByName;
  }

  public void setApprovedByName(String value) {
    approvedByName = value;
  }

  public StockCardDTO() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getTransactionTime() {
    return transactionTime;
  }

  public void setTransactionTime(LocalDateTime transactionTime) {
    this.transactionTime = transactionTime;
  }

  public ChangeType getChangeType() {
    return changeType;
  }

  public void setChangeType(ChangeType changeType) {
    this.changeType = changeType;
  }

  public String getReferenceCode() {
    return referenceCode;
  }

  public void setReferenceCode(String referenceCode) {
    this.referenceCode = referenceCode;
  }

  public String getSkuCode() {
    return skuCode;
  }

  public void setSkuCode(String skuCode) {
    this.skuCode = skuCode;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getBatchCode() {
    return batchCode;
  }

  public void setBatchCode(String batchCode) {
    this.batchCode = batchCode;
  }

  public String getLocationCode() {
    return locationCode;
  }

  public void setLocationCode(String locationCode) {
    this.locationCode = locationCode;
  }

  public BigDecimal getBeforeQty() {
    return beforeQty;
  }

  public void setBeforeQty(BigDecimal beforeQty) {
    this.beforeQty = beforeQty;
  }

  public BigDecimal getChangeQty() {
    return changeQty;
  }

  public void setChangeQty(BigDecimal changeQty) {
    this.changeQty = changeQty;
  }

  public BigDecimal getAfterQty() {
    return afterQty;
  }

  public void setAfterQty(BigDecimal afterQty) {
    this.afterQty = afterQty;
  }

  public String getPerformedByName() {
    return performedByName;
  }

  public void setPerformedByName(String performedByName) {
    this.performedByName = performedByName;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
