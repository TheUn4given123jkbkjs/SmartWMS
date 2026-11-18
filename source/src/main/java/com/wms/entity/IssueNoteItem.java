package com.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Thực thể Chi tiết phân bổ xuất kho Detail (Bảng issue_note_item).
 */
@Entity
@Table(name = "issue_note_item")
public class IssueNoteItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "issue_id", nullable = false)
  private IssueNote issueNote;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "batch_id", nullable = false)
  private StockBatch stockBatch;

  @Column(name = "requested_qty", nullable = false, precision = 12, scale = 2)
  private BigDecimal requestedQty;

  @Column(name = "allocated_qty", nullable = false, precision = 12, scale = 2)
  private BigDecimal allocatedQty = BigDecimal.ZERO;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  public IssueNoteItem() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public IssueNote getIssueNote() {
    return issueNote;
  }

  public void setIssueNote(IssueNote issueNote) {
    this.issueNote = issueNote;
  }

  public StockBatch getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(StockBatch stockBatch) {
    this.stockBatch = stockBatch;
  }

  public BigDecimal getRequestedQty() {
    return requestedQty;
  }

  public void setRequestedQty(BigDecimal requestedQty) {
    this.requestedQty = requestedQty;
  }

  public void setRequestedQty(Integer qty) {
    this.requestedQty = qty != null ? BigDecimal.valueOf(qty) : BigDecimal.ZERO;
  }

  public BigDecimal getAllocatedQty() {
    return allocatedQty;
  }

  public void setAllocatedQty(BigDecimal allocatedQty) {
    this.allocatedQty = allocatedQty;
  }

  public void setAllocatedQty(Integer qty) {
    this.allocatedQty = qty != null ? BigDecimal.valueOf(qty) : BigDecimal.ZERO;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * Kiểm tra lô hàng được phân bổ có còn hạn sử dụng hay không (BR-OUT-03).
   * @return true nếu lô hàng còn hạn hoặc không quản lý hạn, false nếu đã quá hạn.
   */
  public Boolean validateLotNotExpired() {
    return stockBatch == null || !stockBatch.isExpired();
  }
}
