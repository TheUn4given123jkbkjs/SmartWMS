package com.wms.service.impl;

import com.wms.dto.receipt.*;
import com.wms.entity.*;
import com.wms.exception.*;
import com.wms.repository.*;
import com.wms.service.*;
import com.wms.util.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReceiptServiceImpl implements ReceiptService {

  private final ReceiptNoteRepository notes;
  private final StockBatchRepository batches;
  private final WarehouseLocationRepository locations;
  private final PurchaseOrderService orders;
  private final WarehouseOperations ops;

  public ReceiptServiceImpl(
    ReceiptNoteRepository n,
    StockBatchRepository b,
    WarehouseLocationRepository l,
    PurchaseOrderService p,
    WarehouseOperations o
  ) {
    notes = n;
    batches = b;
    locations = l;
    orders = p;
    ops = o;
  }

  public ReceiptNoteResponseDTO createReceiptNote(
    ReceiptNoteCreateDTO dto,
    Long userId
  ) {
    ops.lock();
    User maker = ops.actor(userId, UserRole.ROLE_STAFF);
    PurchaseOrder po = orders.require(dto.getPoNumber());
    if (!"APPROVED".equals(po.getStatus())) throw new ConflictException(
      "PO không còn hiệu lực nhận hàng."
    );
    ReceiptNote note = new ReceiptNote();
    note.setReceiptNumber(ops.code("RN"));
    note.setPoNumber(po.getPoNumber());
    note.setSupplierName(po.getSupplierName());
    note.setNote(dto.getNote());
    note.setMaker(maker);
    Map<Long, BigDecimal> total = new HashMap<>();
    for (ReceiptItemDTO d : dto.getItems()) {
      PurchaseOrderItem order = po
        .getItems()
        .stream()
        .filter(i -> i.getProduct().getId().equals(d.getProductId()))
        .findFirst()
        .orElseThrow(() ->
          new InvalidQuantityException("Mặt hàng không thuộc PO.")
        );
      Product product = order.getProduct();
      if (
        !Boolean.TRUE.equals(product.getActive())
      ) throw new InvalidQuantityException("Mặt hàng đã ngừng hoạt động.");
      BigDecimal qty = ops.qty(d.getActualQty(), true);
      total.merge(product.getId(), qty, BigDecimal::add);
      BigDecimal remaining = order
        .getOrderedQty()
        .subtract(order.getReceivedQty())
        .subtract(orders.pending(po.getPoNumber(), product.getId(), null));
      if (
        total.get(product.getId()).compareTo(remaining) > 0
      ) throw new InvalidQuantityException(
        "Thực nhận vượt số lượng PO còn được nhận: " + remaining
      );
      validateDates(
        product,
        d.getBatchCode(),
        d.getManufactureDate(),
        d.getExpiryDate()
      );
      WarehouseLocation location = locations
        .findById(d.getLocationId())
        .orElseThrow(() ->
          new ResourceNotFoundException("Không tìm thấy vị trí.")
        );
      validateLocation(location, product);
      BigDecimal rejected = ops.qty(
        d.getRejectedQty() == null ? BigDecimal.ZERO : d.getRejectedQty(),
        false
      );
      if (
        rejected.signum() > 0 &&
        (d.getRejectionReason() == null || d.getRejectionReason().isBlank())
      ) throw new InvalidQuantityException("Phải nhập lý do hàng bị từ chối.");
      ReceiptNoteItem item = new ReceiptNoteItem();
      item.setReceiptNote(note);
      item.setProduct(product);
      item.setLocation(location);
      item.setLotNumber(
        d.getBatchCode() == null || d.getBatchCode().isBlank()
          ? ops.code("LOT")
          : d.getBatchCode()
      );
      item.setExpDate(d.getExpiryDate());
      item.setManufactureDate(d.getManufactureDate());
      item.setAcceptedQty(qty);
      item.setDamagedQty(rejected);
      item.setRejectionReason(d.getRejectionReason());
      note.getItems().add(item);
    }
    notes.save(note);
    ops.audit(note.getReceiptNumber(), "CREATE_RECEIPT", maker);
    return map(note);
  }

  private void validateDates(
    Product p,
    String lot,
    LocalDate mfg,
    LocalDate exp
  ) {
    if (
      Boolean.TRUE.equals(p.getManageByLot()) &&
      (lot == null || lot.isBlank() || exp == null)
    ) throw new InvalidQuantityException(
      "Hàng quản lý hạn dùng phải có số lô và EXP."
    );
    if (lot != null && lot.length() > 50) throw new InvalidQuantityException(
      "Số lô tối đa 50 ký tự."
    );
    if (
      exp != null && !exp.isAfter(LocalDate.now())
    ) throw new ExpiredLotException("Hạn dùng nhập kho phải sau hôm nay.");
    if (
      mfg != null &&
      (mfg.isAfter(LocalDate.now()) || (exp != null && mfg.isAfter(exp)))
    ) throw new InvalidQuantityException("Ngày sản xuất không hợp lệ.");
  }

  private void validateLocation(WarehouseLocation location, Product product) {
    if (
      location.getZone() != ZoneType.RAW && location.getZone() != ZoneType.FG
    ) throw new InvalidLocationException("Chỉ được cất hàng ở RAW hoặc FG.");
    if (
      batches
        .findAll()
        .stream()
        .anyMatch(
          b ->
            b.getLocation().getId().equals(location.getId()) &&
            b.getOnHandQty().signum() > 0 &&
            !b.getProduct().getId().equals(product.getId())
        )
    ) throw new InvalidLocationException("Vị trí đang chứa SKU khác.");
  }

  public ReceiptNoteResponseDTO approveReceiptNote(
    Long id,
    ReceiptApprovalDTO decision,
    Long userId
  ) {
    ops.lock();
    User checker = ops.actor(userId, UserRole.ROLE_MANAGER);
    ReceiptNote note = require(id);
    ops.checker(note.getMaker(), checker);
    if (
      note.getStatus() != ReceiptStatus.PENDING_APPROVAL
    ) throw new ConflictException("Phiếu nhập đã được xử lý.");
    note.setChecker(checker);
    note.setApprovedAt(LocalDateTime.now());
    note.setUpdatedAt(LocalDateTime.now());
    if (!Boolean.TRUE.equals(decision.getApproved())) {
      if (
        decision.getReviewNote() == null || decision.getReviewNote().isBlank()
      ) throw new InvalidQuantityException("Từ chối phải có lý do.");
      note.setRejectionNote(decision.getReviewNote());
      note.setStatus(ReceiptStatus.REJECTED);
      ops.audit(note.getReceiptNumber(), "REJECT_RECEIPT", checker);
      return map(note);
    }
    PurchaseOrder po = orders.require(note.getPoNumber());
    if (!"APPROVED".equals(po.getStatus())) throw new ConflictException(
      "PO đã đóng hoặc hủy."
    );
    for (ReceiptNoteItem item : note.getItems()) {
      validateDates(
        item.getProduct(),
        item.getLotNumber(),
        item.getManufactureDate(),
        item.getExpDate()
      );
      validateLocation(item.getLocation(), item.getProduct());
      PurchaseOrderItem line = po
        .getItems()
        .stream()
        .filter(i -> i.getProduct().getId().equals(item.getProduct().getId()))
        .findFirst()
        .orElseThrow();
      if (
        line
          .getReceivedQty()
          .add(item.getAcceptedQty())
          .compareTo(line.getOrderedQty()) >
        0
      ) throw new ConflictException("PO không còn đủ số lượng nhận.");
      StockBatch batch = batches
        .findByProductIdAndLocationIdAndLotNumber(
          item.getProduct().getId(),
          item.getLocation().getId(),
          item.getLotNumber()
        )
        .orElseGet(() -> {
          StockBatch b = new StockBatch();
          b.setProduct(item.getProduct());
          b.setLocation(item.getLocation());
          b.setLotNumber(item.getLotNumber());
          b.setExpDate(item.getExpDate());
          return batches.save(b);
        });
      if (
        !Objects.equals(batch.getExpDate(), item.getExpDate())
      ) throw new ConflictException("Lô cùng mã có hạn dùng khác.");
      batch.setOnHandQty(batch.getOnHandQty().add(item.getAcceptedQty()));
      batch.setUpdatedAt(LocalDateTime.now());
      item.setStockBatch(batch);
      line.setReceivedQty(line.getReceivedQty().add(item.getAcceptedQty()));
      ops.location(item.getLocation());
      ops.card(
        batch,
        item.getAcceptedQty(),
        note.getReceiptNumber(),
        checker,
        checker,
        note.getNote()
      );
    }
    if (
      po
        .getItems()
        .stream()
        .allMatch(i -> i.getReceivedQty().compareTo(i.getOrderedQty()) == 0)
    ) po.setStatus("CLOSED");
    note.setStatus(ReceiptStatus.COMPLETED);
    ops.audit(note.getReceiptNumber(), "COMPLETE_RECEIPT", checker);
    return map(note);
  }

  private ReceiptNote require(Long id) {
    return notes
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("Không tìm thấy phiếu nhập.")
      );
  }

  @Transactional(readOnly = true)
  public List<ReceiptNoteResponseDTO> getReceiptNotes(
    String status,
    String po
  ) {
    return notes
      .findAll()
      .stream()
      .filter(
        n ->
          status == null ||
          status.isBlank() ||
          n.getStatus().name().equals(status)
      )
      .filter(n -> po == null || po.isBlank() || n.getPoNumber().contains(po))
      .map(this::map)
      .toList();
  }

  @Transactional(readOnly = true)
  public ReceiptNoteResponseDTO getReceiptNoteById(Long id) {
    return map(require(id));
  }

  private ReceiptNoteResponseDTO map(ReceiptNote n) {
    ReceiptNoteResponseDTO d = new ReceiptNoteResponseDTO();
    d.setId(n.getId());
    d.setMakerId(n.getMaker().getId());
    d.setMakerName(n.getMaker().getFullName());
    d.setCheckerName(
      n.getChecker() == null ? null : n.getChecker().getFullName()
    );
    d.setReceiptCode(n.getReceiptNumber());
    d.setPoNumber(n.getPoNumber());
    d.setSupplierName(n.getSupplierName());
    d.setNote(
      n.getRejectionNote() != null ? n.getRejectionNote() : n.getNote()
    );
    d.setCreatedAt(n.getCreatedAt());
    d.setApprovedAt(n.getApprovedAt());
    d.setStatus(n.getStatus());
    d.setItems(
      n
        .getItems()
        .stream()
        .map(i -> {
          ReceiptItemDTO r = new ReceiptItemDTO();
          r.setProductId(i.getProduct().getId());
          r.setLocationId(i.getLocation().getId());
          r.setBatchCode(i.getLotNumber());
          r.setManufactureDate(i.getManufactureDate());
          r.setExpiryDate(i.getExpDate());
          r.setActualQty(i.getAcceptedQty());
          r.setRejectedQty(i.getDamagedQty());
          r.setRejectionReason(i.getRejectionReason());
          return r;
        })
        .toList()
    );
    return d;
  }
}
