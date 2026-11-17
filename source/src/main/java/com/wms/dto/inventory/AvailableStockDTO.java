package com.wms.dto.inventory;

import java.math.BigDecimal;

public class AvailableStockDTO {

  private Long productId;
  private String skuCode;
  private String productName;
  private String uom;
  private BigDecimal totalOnHandQty;
  private BigDecimal totalReservedQty;
  private BigDecimal availableQty;
  private BigDecimal minStockAlert;
  private Boolean underMin;

  private BigDecimal onOrder;

  public BigDecimal getOnOrder() {
    return onOrder;
  }

  public void setOnOrder(BigDecimal value) {
    onOrder = value;
  }

  private BigDecimal maxStock;

  public BigDecimal getMaxStock() {
    return maxStock;
  }

  public void setMaxStock(BigDecimal value) {
    maxStock = value;
  }

  private BigDecimal suggestedOrderQty;

  public BigDecimal getSuggestedOrderQty() {
    return suggestedOrderQty;
  }

  public void setSuggestedOrderQty(BigDecimal value) {
    suggestedOrderQty = value;
  }

  public AvailableStockDTO() {}

  public AvailableStockDTO(
    Long productId,
    String skuCode,
    String productName,
    String uom,
    BigDecimal totalOnHandQty,
    BigDecimal totalReservedQty,
    BigDecimal availableQty,
    BigDecimal minStockAlert,
    Boolean underMin
  ) {
    this.productId = productId;
    this.skuCode = skuCode;
    this.productName = productName;
    this.uom = uom;
    this.totalOnHandQty = totalOnHandQty;
    this.totalReservedQty = totalReservedQty;
    this.availableQty = availableQty;
    this.minStockAlert = minStockAlert;
    this.underMin = underMin;
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

  public BigDecimal getTotalOnHandQty() {
    return totalOnHandQty;
  }

  public void setTotalOnHandQty(BigDecimal totalOnHandQty) {
    this.totalOnHandQty = totalOnHandQty;
  }

  public BigDecimal getTotalReservedQty() {
    return totalReservedQty;
  }

  public void setTotalReservedQty(BigDecimal totalReservedQty) {
    this.totalReservedQty = totalReservedQty;
  }

  public BigDecimal getAvailableQty() {
    return availableQty;
  }

  public void setAvailableQty(BigDecimal availableQty) {
    this.availableQty = availableQty;
  }

  public BigDecimal getMinStockAlert() {
    return minStockAlert;
  }

  public void setMinStockAlert(BigDecimal minStockAlert) {
    this.minStockAlert = minStockAlert;
  }

  public Boolean getUnderMin() {
    return underMin;
  }

  public void setUnderMin(Boolean underMin) {
    this.underMin = underMin;
  }
}
