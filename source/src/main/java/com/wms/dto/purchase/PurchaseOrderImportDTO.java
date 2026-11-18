package com.wms.dto.purchase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO tiếp nhận dữ liệu Đơn mua hàng PO hợp lệ từ bên ngoài hoặc Quản lý nhập.
 */
public class PurchaseOrderImportDTO {

  @NotBlank(message = "Mã PO không được để trống")
  @Size(max = 50, message = "Mã PO tối đa 50 ký tự")
  private String poNumber;

  @NotBlank(message = "Tên nhà cung cấp không được để trống")
  @Size(max = 255, message = "Tên nhà cung cấp tối đa 255 ký tự")
  private String supplierName;

  @NotEmpty(message = "Danh sách mặt hàng PO không được để trống")
  @Valid
  private List<Item> items = new ArrayList<>();

  public static class Item {
    @NotNull(message = "ID sản phẩm không được để trống")
    private Long productId;

    @NotNull(message = "Số lượng đặt không được để trống")
    @DecimalMin(value = "0.01", message = "Số lượng đặt phải lớn hơn 0")
    private BigDecimal orderedQty;

    public Item() {}

    public Item(Long productId, BigDecimal orderedQty) {
      this.productId = productId;
      this.orderedQty = orderedQty;
    }

    public Long getProductId() {
      return productId;
    }

    public void setProductId(Long productId) {
      this.productId = productId;
    }

    public BigDecimal getOrderedQty() {
      return orderedQty;
    }

    public void setOrderedQty(BigDecimal orderedQty) {
      this.orderedQty = orderedQty;
    }
  }

  public PurchaseOrderImportDTO() {}

  public PurchaseOrderImportDTO(String poNumber, String supplierName, List<Item> items) {
    this.poNumber = poNumber;
    this.supplierName = supplierName;
    this.items = items;
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

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }
}
