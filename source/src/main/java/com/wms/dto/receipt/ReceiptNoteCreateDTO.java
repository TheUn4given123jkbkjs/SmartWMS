package com.wms.dto.receipt;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public class ReceiptNoteCreateDTO {
    @NotBlank(message = "Số Purchase Order (PO) bắt buộc")
    private String poNumber;

    @NotBlank(message = "Tên nhà cung cấp bắt buộc")
    private String supplierName;

    @Size(max = 500, message = "Ghi chú tối đa 500 ký tự")
    private String note;

    @NotEmpty(message = "Phiếu nhập phải có ít nhất 1 dòng mặt hàng")
    @Valid
    private List<ReceiptItemDTO> items;

    public ReceiptNoteCreateDTO() {}

    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public List<ReceiptItemDTO> getItems() { return items; }
    public void setItems(List<ReceiptItemDTO> items) { this.items = items; }
}

