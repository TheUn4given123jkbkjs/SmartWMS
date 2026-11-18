package com.wms.service;

import com.wms.entity.*;
import com.wms.exception.*;
import com.wms.repository.*;
import com.wms.util.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import org.springframework.stereotype.Service;

/** Helpers used only inside the calling service's transaction. */
@Service
public class WarehouseOperations {

  @PersistenceContext
  private EntityManager em;

  private final UserRepository users;
  private final StockBatchRepository batches;
  private final StockCardRepository cards;

  public WarehouseOperations(
    UserRepository users,
    StockBatchRepository batches,
    StockCardRepository cards
  ) {
    this.users = users;
    this.batches = batches;
    this.cards = cards;
  }

  public void lock() {
    if (
      em.find(WarehouseMutex.class, 1L, LockModeType.PESSIMISTIC_WRITE) == null
    ) throw new IllegalStateException("Warehouse lock was not initialized.");
  }

  public User actor(Long id, UserRole role) {
    User u = users
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("Không tìm thấy người dùng.")
      );
    if (
      !Boolean.TRUE.equals(u.getActive()) || u.getRole() != role
    ) throw new SecurityViolationException(
      "Vai trò không được phép thực hiện thao tác này."
    );
    return u;
  }

  public void checker(User maker, User checker) {
    if (
      maker.getId().equals(checker.getId())
    ) throw new SecurityViolationException(
      "Người lập không được tự phê duyệt."
    );
  }

  public BigDecimal qty(BigDecimal v, boolean positive) {
    if (
      v == null ||
      (positive ? v.signum() <= 0 : v.signum() < 0) ||
      v.scale() > 2 ||
      v.precision() - v.scale() > 10
    ) throw new InvalidQuantityException(
      "Số lượng phải " +
      (positive ? "lớn hơn 0" : "không âm") +
      ", tối đa 2 chữ số thập phân."
    );
    return v;
  }

  public String code(String prefix) {
    return prefix + "-" + java.util.UUID.randomUUID().toString();
  }

  public BigDecimal onHand(Product p) {
    return batches
      .findByProductId(p.getId())
      .stream()
      .map(StockBatch::getOnHandQty)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public void location(WarehouseLocation location) {
    boolean occupied = batches
      .findAll()
      .stream()
      .anyMatch(
        b ->
          b.getLocation().getId().equals(location.getId()) &&
          b.getOnHandQty().signum() > 0
      );
    location.setStatus(
      occupied ? LocationStatus.OCCUPIED : LocationStatus.EMPTY
    );
    location.setUpdatedAt(LocalDateTime.now());
  }

  public void card(
    StockBatch batch,
    BigDecimal change,
    String reference,
    User actor,
    User checker,
    String reason
  ) {
    StockCard card = new StockCard();
    card.setProduct(batch.getProduct());
    card.setStockBatch(batch);
    card.setQtyChanged(change);
    card.setAfterChanged(onHand(batch.getProduct()));
    card.setChangeType(
      change.signum() > 0 ? ChangeType.NHAP_PO : ChangeType.XUAT_KHO
    );
    card.setReferenceCode(reference);
    card.setPerformedBy(actor);
    card.setApprovedBy(checker);
    card.setReason(reason);
    cards.save(card);
  }

  public void audit(String reference, String action, User actor) {
    AuditEvent e = new AuditEvent();
    e.setReferenceCode(reference);
    e.setAction(action);
    e.setActor(actor);
    em.persist(e);
  }
}
