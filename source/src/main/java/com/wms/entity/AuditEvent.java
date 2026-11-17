package com.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AuditEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String referenceCode;

  @Column(nullable = false)
  private String action;

  @ManyToOne(optional = false)
  private User actor;

  @Column(nullable = false, updatable = false)
  private LocalDateTime occurredAt = LocalDateTime.now();

  public Long getId() {
    return id;
  }

  public String getReferenceCode() {
    return referenceCode;
  }

  public void setReferenceCode(String v) {
    referenceCode = v;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String v) {
    action = v;
  }

  public User getActor() {
    return actor;
  }

  public void setActor(User v) {
    actor = v;
  }

  public LocalDateTime getOccurredAt() {
    return occurredAt;
  }
}
