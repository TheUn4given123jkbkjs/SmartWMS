package com.wms.dto.receipt;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReceiptApprovalDTO {

  @NotNull(message = "Trạng thái quyết định duyệt bắt buộc")
  private Boolean approved;

  @Size(max = 255, message = "Ý kiến đánh giá tối đa 255 ký tự")
  private String reviewNote;

  public ReceiptApprovalDTO() {}

  public ReceiptApprovalDTO(Boolean approved, String reviewNote) {
    this.approved = approved;
    this.reviewNote = reviewNote;
  }

  public Boolean getApproved() {
    return approved;
  }

  public void setApproved(Boolean approved) {
    this.approved = approved;
  }

  public String getReviewNote() {
    return reviewNote;
  }

  public void setReviewNote(String reviewNote) {
    this.reviewNote = reviewNote;
  }
}
