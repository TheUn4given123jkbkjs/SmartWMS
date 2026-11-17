package com.wms.dto.issue;

import com.wms.util.DispatchStrategy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class IssueRequestDTO {

  @NotBlank(message = "Mã lệnh xuất kho bắt buộc")
  private String orderNumber;

  @NotBlank(message = "Tên khách hàng / đơn vị nhận bắt buộc")
  private String customerName;

  @NotNull(message = "Chiến lược xuất bắt buộc (FEFO hoặc FIFO)")
  private DispatchStrategy strategy;

  @Size(max = 500, message = "Ghi chú tối đa 500 ký tự")
  private String note;

  @NotEmpty(message = "Phiếu xuất phải có ít nhất 1 dòng mặt hàng")
  @Valid
  private List<IssueItemRequestDTO> items;

  @Size(max = 255)
  private String overrideReason;

  public String getOverrideReason() {
    return overrideReason;
  }

  public void setOverrideReason(String value) {
    overrideReason = value;
  }

  @jakarta.validation.constraints.Pattern(regexp = "PRODUCTION|SALE")
  @NotNull
  private String exportType = "SALE";

  public String getExportType() {
    return exportType;
  }

  public void setExportType(String value) {
    exportType = value;
  }

  public IssueRequestDTO() {}

  public String getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public DispatchStrategy getStrategy() {
    return strategy;
  }

  public void setStrategy(DispatchStrategy strategy) {
    this.strategy = strategy;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public List<IssueItemRequestDTO> getItems() {
    return items;
  }

  public void setItems(List<IssueItemRequestDTO> items) {
    this.items = items;
  }
}
