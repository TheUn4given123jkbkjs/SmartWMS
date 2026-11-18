package com.wms.dto.purchase;

import java.math.BigDecimal;

/**
 * DTO hiển thị chi tiết mặt hàng trên đơn mua hàng PO.
 */
public class PurchaseOrderItemDTO {

  private Long productId;
  private String skuCode;
  private String productName;
  private String uom;
  private BigDecimal orderedQty;
  private BigDecimal receivedQty;
  private BigDecimal pendingQty;
  private BigDecimal remainingQty;

  public PurchaseOrderItemDTO() {}

  public PurchaseOrderItemDTO(
    Long productId,
    String skuCode,
    String productName,
    String uom,
    BigDecimal orderedQty,
    BigDecimal receivedQty,
    BigDecimal pendingQty,
    BigDecimal remainingQty
  ) {
    this.productId = productId;
    this.skuCode = skuCode;
    this.productName = productName;
    this.uom = uom;
    this.orderedQty = orderedQty;
    this.receivedQty = receivedQty;
    this.pendingQty = pendingQty;
    this.remainingQty = remainingQty;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
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

  public String getUom() {
    return uom;
  }

  public void setUom(String uom) {
    this.uom = uom;
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

  public BigDecimal getPendingQty() {
    return pendingQty;
  }

  public void setPendingQty(BigDecimal pendingQty) {
    this.pendingQty = pendingQty;
  }

  public BigDecimal getRemainingQty() {
    return remainingQty;
  }

  public void setRemainingQty(BigDecimal remainingQty) {
    this.remainingQty = remainingQty;
  }
}
