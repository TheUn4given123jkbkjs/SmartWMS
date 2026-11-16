package com.wms.entity;

import com.wms.util.DispatchStrategy;
import com.wms.util.IssueStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Thực thể Phiếu xuất kho Header (Bảng issue_note).
 */
@Entity
@Table(name = "issue_note", uniqueConstraints = {
    @UniqueConstraint(name = "uk_issue_number", columnNames = {"issue_number"})
})
public class IssueNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issue_number", nullable = false, length = 50)
    private String issueNumber;

    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "dispatch_strategy", nullable = false, length = 10)
    private DispatchStrategy dispatchStrategy = DispatchStrategy.FEFO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maker_id", nullable = false)
    private User maker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checker_id")
    private User checker;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private IssueStatus status = IssueStatus.PENDING_APPROVAL;

    @Column(name = "is_overridden", nullable = false)
    private Boolean isOverridden = false;

    @Column(name = "override_reason", length = 255)
    private String overrideReason;

    @OneToMany(mappedBy = "issueNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueNoteItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public IssueNote() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIssueNumber() { return issueNumber; }
    public void setIssueNumber(String issueNumber) { this.issueNumber = issueNumber; }

    public LocalDateTime getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDateTime issueDate) { this.issueDate = issueDate; }

    public DispatchStrategy getDispatchStrategy() { return dispatchStrategy; }
    public void setDispatchStrategy(DispatchStrategy dispatchStrategy) { this.dispatchStrategy = dispatchStrategy; }

    public User getMaker() { return maker; }
    public void setMaker(User maker) { this.maker = maker; }

    public User getChecker() { return checker; }
    public void setChecker(User checker) { this.checker = checker; }

    public IssueStatus getStatus() { return status; }
    public void setStatus(IssueStatus status) { this.status = status; }

    public Boolean getIsOverridden() { return isOverridden; }
    public void setIsOverridden(Boolean isOverridden) { this.isOverridden = isOverridden; }

    public String getOverrideReason() { return overrideReason; }
    public void setOverrideReason(String overrideReason) { this.overrideReason = overrideReason; }

    public List<IssueNoteItem> getItems() { return items; }
    public void setItems(List<IssueNoteItem> items) { this.items = items; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
