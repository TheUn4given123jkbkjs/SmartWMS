package com.wms.dto.catalog;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class ProductCreateDTO {

  @NotBlank(message = "Mã SKU bắt buộc nhập")
  @Size(min = 3, max = 30)
  @jakarta.validation.constraints.Pattern(regexp = "[A-Z0-9-]+")
  private String skuCode;

  @NotBlank(message = "Tên hàng hóa bắt buộc nhập")
  @Size(max = 255, message = "Tên hàng hóa tối đa 255 ký tự")
  private String name;

  @NotNull(message = "Phải chọn ngành hàng")
  private Long categoryId;

  @NotBlank(message = "Đơn vị tính bắt buộc")
  @Size(max = 20)
  private String uom;

  @jakarta.validation.constraints.Digits(integer = 10, fraction = 2)
  private BigDecimal minStock = BigDecimal.ZERO;

  @jakarta.validation.constraints.Digits(integer = 10, fraction = 2)
  private BigDecimal maxStock = new BigDecimal("1000");

  private Boolean manageByLot = true;

  @jakarta.validation.constraints.Digits(integer = 10, fraction = 2)
  private BigDecimal minStockAlert;

  @jakarta.validation.constraints.Digits(integer = 10, fraction = 2)
  private BigDecimal maxWeightPerUnit;

  private Integer shelfLifeDays;

  public ProductCreateDTO() {}

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

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getUom() {
    return uom;
  }

  public void setUom(String uom) {
    this.uom = uom;
  }

  public BigDecimal getMinStock() {
    if (minStock != null) return minStock;
    if (minStockAlert != null) return minStockAlert;
    return BigDecimal.ZERO;
  }

  public void setMinStock(BigDecimal minStock) {
    this.minStock = minStock;
    if (minStock != null) this.minStockAlert = minStock;
  }

  public BigDecimal getMaxStock() {
    if (maxStock != null) return maxStock;
    return new BigDecimal("1000");
  }

  public void setMaxStock(BigDecimal maxStock) {
    this.maxStock = maxStock;
  }

  public Boolean getManageByLot() {
    return manageByLot != null ? manageByLot : true;
  }

  public void setManageByLot(Boolean manageByLot) {
    this.manageByLot = manageByLot;
  }

  public BigDecimal getMinStockAlert() {
    return minStockAlert;
  }

  public void setMinStockAlert(BigDecimal minStockAlert) {
    this.minStockAlert = minStockAlert;
    if (minStockAlert != null) this.minStock = minStockAlert;
  }

  public BigDecimal getMaxWeightPerUnit() {
    return maxWeightPerUnit;
  }

  public void setMaxWeightPerUnit(BigDecimal maxWeightPerUnit) {
    this.maxWeightPerUnit = maxWeightPerUnit;
  }

  public Integer getShelfLifeDays() {
    return shelfLifeDays;
  }

  public void setShelfLifeDays(Integer shelfLifeDays) {
    this.shelfLifeDays = shelfLifeDays;
  }
}
