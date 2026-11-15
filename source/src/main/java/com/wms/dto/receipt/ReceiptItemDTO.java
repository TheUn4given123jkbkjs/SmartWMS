package com.wms.dto.receipt;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ReceiptItemDTO {
    @NotNull(message = "Mã sản phẩm bắt buộc")
    private Long productId;

    @NotNull(message = "Vị trí ô kệ lưu trữ bắt buộc")
    private Long locationId;

    @NotBlank(message = "Số lô bắt buộc")
    private String batchCode;

    @PastOrPresent(message = "Ngày sản xuất không thể ở tương lai")
    private LocalDate manufactureDate;

    @Future(message = "Hạn sử dụng phải ở tương lai")
    private LocalDate expiryDate;

    @NotNull(message = "Số lượng đặt hàng PO bắt buộc")
    @Min(value = 1, message = "Số lượng kế hoạch phải lớn hơn 0")
    private BigDecimal plannedQty;

    @NotNull(message = "Số lượng thực tế bắt buộc")
    @Min(value = 1, message = "Số lượng thực nhập phải lớn hơn 0")
    private BigDecimal actualQty;

    @NotNull(message = "Đơn giá bắt buộc")
    @Min(value = 0, message = "Đơn giá không được âm")
    private BigDecimal unitPrice;

    private String note;

    public ReceiptItemDTO() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    public String getBatchCode() { return batchCode; }
    public void setBatchCode(String batchCode) { this.batchCode = batchCode; }

    public LocalDate getManufactureDate() { return manufactureDate; }
    public void setManufactureDate(LocalDate manufactureDate) { this.manufactureDate = manufactureDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public BigDecimal getPlannedQty() { return plannedQty; }
    public void setPlannedQty(BigDecimal plannedQty) { this.plannedQty = plannedQty; }

    public BigDecimal getActualQty() { return actualQty; }
    public void setActualQty(BigDecimal actualQty) { this.actualQty = actualQty; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}

