package com.wms.entity;

import jakarta.persistence.*;

/** A database lock for stock-affecting transactions in this single-warehouse application. */
@Entity
public class WarehouseMutex {

  @Id
  private Long id;

  public WarehouseMutex() {}

  public WarehouseMutex(Long id) {
    this.id = id;
  }
}
