package com.wms.dto.issue;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class IssueItemRequestDTO {
    @NotNull(message = "Mã sản phẩm bắt buộc")
    private Long productId;

    @NotNull(message = "Số lượng yêu cầu xuất bắt buộc")
    @Min(value = 1, message = "Số lượng yêu cầu phải lớn hơn 0")
    private BigDecimal requestedQty;

    private Long selectedBatchId;

    public IssueItemRequestDTO() {}

    public IssueItemRequestDTO(Long productId, BigDecimal requestedQty) {
        this.productId = productId;
        this.requestedQty = requestedQty;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public BigDecimal getRequestedQty() { return requestedQty; }
    public void setRequestedQty(BigDecimal requestedQty) { this.requestedQty = requestedQty; }

    public Long getSelectedBatchId() { return selectedBatchId; }
    public void setSelectedBatchId(Long selectedBatchId) { this.selectedBatchId = selectedBatchId; }
}

