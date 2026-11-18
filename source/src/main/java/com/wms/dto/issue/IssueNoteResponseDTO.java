package com.wms.dto.issue;

import com.wms.util.DispatchStrategy;
import com.wms.util.IssueStatus;
import java.time.LocalDateTime;
import java.util.List;

public class IssueNoteResponseDTO {

  private Long makerId;

  public Long getMakerId() {
    return makerId;
  }

  public void setMakerId(Long id) {
    makerId = id;
  }

  private Long id;
  private String issueCode;
  private String orderNumber;
  private String customerName;
  private DispatchStrategy strategy;
  private String makerName;
  private String checkerName;
  private IssueStatus status;
  private String note;
  private LocalDateTime createdAt;
  private LocalDateTime approvedAt;
  private List<PickListProposalDTO> pickList;

  private String overrideReason;

  public String getOverrideReason() {
    return overrideReason;
  }

  public void setOverrideReason(String value) {
    overrideReason = value;
  }

  private String decisionReason;

  public String getDecisionReason() {
    return decisionReason;
  }

  public void setDecisionReason(String value) {
    decisionReason = value;
  }

  private String exportType;

  public String getExportType() {
    return exportType;
  }

  public void setExportType(String value) {
    exportType = value;
  }

  private String confirmedByName;

  public String getConfirmedByName() {
    return confirmedByName;
  }

  public void setConfirmedByName(String value) {
    confirmedByName = value;
  }

  private LocalDateTime completedAt;

  public LocalDateTime getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(LocalDateTime value) {
    completedAt = value;
  }

  public IssueNoteResponseDTO() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIssueCode() {
    return issueCode;
  }

  public void setIssueCode(String issueCode) {
    this.issueCode = issueCode;
  }

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

  public String getMakerName() {
    return makerName;
  }

  public void setMakerName(String makerName) {
    this.makerName = makerName;
  }

  public String getCheckerName() {
    return checkerName;
  }

  public void setCheckerName(String checkerName) {
    this.checkerName = checkerName;
  }

  public IssueStatus getStatus() {
    return status;
  }

  public void setStatus(IssueStatus status) {
    this.status = status;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getApprovedAt() {
    return approvedAt;
  }

  public void setApprovedAt(LocalDateTime approvedAt) {
    this.approvedAt = approvedAt;
  }

  public List<PickListProposalDTO> getPickList() {
    return pickList;
  }

  public void setPickList(List<PickListProposalDTO> pickList) {
    this.pickList = pickList;
  }
}
