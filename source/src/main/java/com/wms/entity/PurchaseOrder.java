package com.wms.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class PurchaseOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String poNumber;

  @Column(nullable = false)
  private String supplierName;

  @Column(nullable = false)
  private String status = "APPROVED";

  @OneToMany(
    mappedBy = "purchaseOrder",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<PurchaseOrderItem> items = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public String getPoNumber() {
    return poNumber;
  }

  public void setPoNumber(String v) {
    poNumber = v;
  }

  public String getSupplierName() {
    return supplierName;
  }

  public void setSupplierName(String v) {
    supplierName = v;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String v) {
    status = v;
  }

  public List<PurchaseOrderItem> getItems() {
    return items;
  }
}
