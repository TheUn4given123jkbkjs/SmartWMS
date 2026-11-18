package com.wms.entity;

import jakarta.persistence.*;
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

    @Column(name = "requested_qty", nullable = false)
    private Integer requestedQty;

    @Column(name = "allocated_qty", nullable = false)
    private Integer allocatedQty = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public IssueNoteItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public IssueNote getIssueNote() { return issueNote; }
    public void setIssueNote(IssueNote issueNote) { this.issueNote = issueNote; }

    public StockBatch getStockBatch() { return stockBatch; }
    public void setStockBatch(StockBatch stockBatch) { this.stockBatch = stockBatch; }

    public Integer getRequestedQty() { return requestedQty; }
    public void setRequestedQty(Integer requestedQty) { this.requestedQty = requestedQty; }

    public Integer getAllocatedQty() { return allocatedQty; }
    public void setAllocatedQty(Integer allocatedQty) { this.allocatedQty = allocatedQty; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * Kiểm tra lô hàng được phân bổ có còn hạn sử dụng hay không (BR-OUT-03).
     * @return true nếu lô hàng còn hạn hoặc không quản lý hạn, false nếu đã quá hạn.
     */
    public Boolean validateLotNotExpired() {
        return stockBatch == null || !stockBatch.isExpired();
    }
}
