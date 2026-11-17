package com.wms.service.impl;

import com.wms.dto.issue.*;
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
public class IssueServiceImpl implements IssueService {

  private final IssueNoteRepository notes;
  private final ProductRepository products;
  private final StockBatchRepository batches;
  private final WarehouseOperations ops;

  public IssueServiceImpl(
    IssueNoteRepository n,
    ProductRepository p,
    StockBatchRepository b,
    WarehouseOperations o
  ) {
    notes = n;
    products = p;
    batches = b;
    ops = o;
  }

  private Product product(Long id) {
    return products
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("Không tìm thấy sản phẩm.")
      );
  }

  private List<StockBatch> ordered(Product p, DispatchStrategy strategy) {
    Comparator<StockBatch> age = Comparator.comparing(
      StockBatch::getCreatedAt
    ).thenComparing(StockBatch::getId);
    Comparator<StockBatch> sort = Boolean.TRUE.equals(p.getManageByLot()) &&
      strategy != DispatchStrategy.FIFO
      ? Comparator.comparing(
        StockBatch::getExpDate,
        Comparator.nullsLast(Comparator.naturalOrder())
      ).thenComparing(age)
      : age;
    return batches
      .findByProductId(p.getId())
      .stream()
      .filter(b -> b.isEligible() && b.getAvailableQty().signum() > 0)
      .sorted(sort)
      .toList();
  }

  private PickListProposalDTO pick(StockBatch b, BigDecimal q) {
    return new PickListProposalDTO(
      b.getProduct().getId(),
      b.getProduct().getSkuCode(),
      b.getProduct().getName(),
      b.getId(),
      b.getLotNumber(),
      b.getLocation().getLocationCode(),
      b.getExpDate(),
      q
    );
  }

  @Transactional(readOnly = true)
  public List<PickListProposalDTO> proposeAllocation(
    Long id,
    BigDecimal qty,
    DispatchStrategy strategy
  ) {
    ops.qty(qty, true);
    Product p = product(id);
    List<PickListProposalDTO> result = new ArrayList<>();
    BigDecimal remaining = qty;
    for (StockBatch b : ordered(p, strategy)) {
      BigDecimal q = remaining.min(b.getAvailableQty());
      result.add(pick(b, q));
      remaining = remaining.subtract(q);
      if (remaining.signum() == 0) break;
    }
    if (remaining.signum() > 0) throw new InsufficientStockException(
      "Không đủ tồn khả dụng. Còn thiếu: " + remaining
    );
    return result;
  }

  public IssueNoteResponseDTO createIssueNote(
    IssueRequestDTO dto,
    Long userId
  ) {
    ops.lock();
    User maker = ops.actor(userId, UserRole.ROLE_STAFF);
    IssueNote n = new IssueNote();
    n.setIssueNumber(ops.code("IN"));
    n.setOrderNumber(dto.getOrderNumber());
    n.setCustomerName(dto.getCustomerName());
    n.setNote(dto.getNote());
    n.setExportType(dto.getExportType());
    n.setDispatchStrategy(dto.getStrategy());
    n.setMaker(maker);
    for (IssueItemRequestDTO d : dto.getItems()) {
      Product p = product(d.getProductId());
      if (
        !Boolean.TRUE.equals(p.getActive())
      ) throw new InvalidQuantityException("Sản phẩm đã ngừng hoạt động.");
      BigDecimal remaining = ops.qty(d.getRequestedQty(), true);
      List<StockBatch> order = ordered(p, dto.getStrategy());
      boolean overridden =
        Boolean.TRUE.equals(p.getManageByLot()) &&
        dto.getStrategy() == DispatchStrategy.FIFO;
      if (d.getSelectedBatchId() != null) {
        StockBatch selected = batches
          .findById(d.getSelectedBatchId())
          .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lô.")
          );
        if (
          !selected.getProduct().getId().equals(p.getId()) ||
          !selected.isEligible()
        ) throw new ExpiredLotException(
          "Lô không thuộc mặt hàng hoặc không đủ điều kiện xuất."
        );
        overridden =
          overridden ||
          order.isEmpty() ||
          !order.get(0).getId().equals(selected.getId());
        order = List.of(selected);
      }
      if (overridden) {
        if (
          dto.getOverrideReason() == null || dto.getOverrideReason().isBlank()
        ) throw new InvalidQuantityException(
          "Đổi thứ tự ưu tiên phải nhập lý do ngoại lệ."
        );
        n.setIsOverridden(true);
        n.setOverrideReason(dto.getOverrideReason());
      }
      for (StockBatch batch : order) {
        BigDecimal q = remaining.min(batch.getAvailableQty());
        if (q.signum() <= 0) continue;
        batch.setReservedQty(batch.getReservedQty().add(q));
        batch.setUpdatedAt(LocalDateTime.now());
        IssueNoteItem item = new IssueNoteItem();
        item.setIssueNote(n);
        item.setStockBatch(batch);
        item.setRequestedQty(q);
        item.setAllocatedQty(q);
        n.getItems().add(item);
        remaining = remaining.subtract(q);
        if (remaining.signum() == 0) break;
      }
      if (remaining.signum() > 0) throw new InsufficientStockException(
        "Không đủ tồn khả dụng. Còn thiếu: " + remaining
      );
    }
    notes.save(n);
    ops.audit(n.getIssueNumber(), "RESERVE_ISSUE", maker);
    return map(n);
  }

  private IssueNote require(Long id) {
    return notes
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("Không tìm thấy phiếu xuất.")
      );
  }

  private void pending(IssueNote n) {
    if (
      n.getStatus() != IssueStatus.PENDING_APPROVAL
    ) throw new ConflictException("Phiếu không còn chờ duyệt.");
  }

  public IssueNoteResponseDTO approveIssueNote(Long id, Long userId) {
    ops.lock();
    User checker = ops.actor(userId, UserRole.ROLE_MANAGER);
    IssueNote n = require(id);
    ops.checker(n.getMaker(), checker);
    pending(n);
    for (IssueNoteItem i : n.getItems()) if (
      !i.getStockBatch().isEligible()
    ) throw new ExpiredLotException(
      "Phiếu có lô đã quá hạn hoặc bị cách ly. Hãy từ chối/hủy để giải phóng giữ chỗ."
    );
    n.setChecker(checker);
    n.setApprovedAt(LocalDateTime.now());
    n.setUpdatedAt(LocalDateTime.now());
    n.setStatus(IssueStatus.APPROVED_READY_TO_PICK);
    ops.audit(n.getIssueNumber(), "APPROVE_ISSUE", checker);
    return map(n);
  }

  public IssueNoteResponseDTO completeIssueNote(Long id, Long userId) {
    ops.lock();
    User actor = ops.actor(userId, UserRole.ROLE_STAFF);
    IssueNote n = require(id);
    if (
      n.getStatus() != IssueStatus.APPROVED_READY_TO_PICK
    ) throw new ConflictException(
      "Phiếu phải được quản lý duyệt trước khi bàn giao."
    );
    for (IssueNoteItem i : n.getItems()) {
      StockBatch b = i.getStockBatch();
      BigDecimal q = i.getAllocatedQty();
      if (!b.isEligible()) throw new ExpiredLotException(
        "Lô đã hết hạn hoặc không đủ điều kiện xuất."
      );
      if (
        b.getReservedQty().compareTo(q) < 0 || b.getOnHandQty().compareTo(q) < 0
      ) throw new ConflictException("Số dư tồn kho/giữ chỗ không hợp lệ.");
      b.setOnHandQty(b.getOnHandQty().subtract(q));
      b.setReservedQty(b.getReservedQty().subtract(q));
      b.setUpdatedAt(LocalDateTime.now());
      ops.location(b.getLocation());
      ops.card(
        b,
        q.negate(),
        n.getIssueNumber(),
        actor,
        n.getChecker(),
        n.getNote()
      );
    }
    n.setStatus(IssueStatus.COMPLETED);
    n.setConfirmedBy(actor);
    n.setCompletedAt(LocalDateTime.now());
    n.setUpdatedAt(LocalDateTime.now());
    ops.audit(n.getIssueNumber(), "COMPLETE_ISSUE", actor);
    return map(n);
  }

  public IssueNoteResponseDTO rejectIssueNote(
    Long id,
    String reason,
    Long userId
  ) {
    ops.lock();
    User actor = ops.actor(userId, UserRole.ROLE_MANAGER);
    IssueNote n = require(id);
    ops.checker(n.getMaker(), actor);
    pending(n);
    release(n, reason, actor, IssueStatus.REJECTED);
    n.setChecker(actor);
    return map(n);
  }

  public void cancelIssueNote(Long id, String reason, Long userId) {
    ops.lock();
    IssueNote n = require(id);
    User actor;
    if (n.getMaker().getId().equals(userId)) actor = ops.actor(
      userId,
      UserRole.ROLE_STAFF
    );
    else actor = ops.actor(userId, UserRole.ROLE_MANAGER);
    if (
      n.getStatus() != IssueStatus.PENDING_APPROVAL &&
      n.getStatus() != IssueStatus.APPROVED_READY_TO_PICK
    ) throw new ConflictException(
      "Không thể hủy phiếu đã hoàn tất hoặc đã hủy/từ chối."
    );
    release(n, reason, actor, IssueStatus.CANCELLED);
  }

  private void release(
    IssueNote n,
    String reason,
    User actor,
    IssueStatus status
  ) {
    if (
      reason == null || reason.isBlank() || reason.length() > 255
    ) throw new InvalidQuantityException("Phải nhập lý do (tối đa 255 ký tự).");
    for (IssueNoteItem i : n.getItems()) {
      StockBatch b = i.getStockBatch();
      if (
        b.getReservedQty().compareTo(i.getAllocatedQty()) < 0
      ) throw new ConflictException("Số giữ chỗ không hợp lệ.");
      b.setReservedQty(b.getReservedQty().subtract(i.getAllocatedQty()));
      b.setUpdatedAt(LocalDateTime.now());
    }
    n.setStatus(status);
    n.setDecisionReason(reason);
    n.setUpdatedAt(LocalDateTime.now());
    ops.audit(n.getIssueNumber(), status.name(), actor);
  }

  @Transactional(readOnly = true)
  public List<IssueNoteResponseDTO> getIssueNotes(String status) {
    return notes
      .findAll()
      .stream()
      .filter(
        n ->
          status == null ||
          status.isBlank() ||
          n.getStatus().name().equals(status)
      )
      .map(this::map)
      .toList();
  }

  @Transactional(readOnly = true)
  public IssueNoteResponseDTO getIssueNoteById(Long id) {
    return map(require(id));
  }

  private IssueNoteResponseDTO map(IssueNote n) {
    IssueNoteResponseDTO d = new IssueNoteResponseDTO();
    d.setId(n.getId());
    d.setMakerId(n.getMaker().getId());
    d.setIssueCode(n.getIssueNumber());
    d.setOrderNumber(n.getOrderNumber());
    d.setCustomerName(n.getCustomerName());
    d.setStrategy(n.getDispatchStrategy());
    d.setNote(n.getNote());
    d.setMakerName(n.getMaker().getFullName());
    d.setCheckerName(
      n.getChecker() == null ? null : n.getChecker().getFullName()
    );
    d.setCreatedAt(n.getCreatedAt());
    d.setApprovedAt(n.getApprovedAt());
    d.setStatus(n.getStatus());
    d.setPickList(
      n
        .getItems()
        .stream()
        .map(i -> pick(i.getStockBatch(), i.getAllocatedQty()))
        .toList()
    );
    d.setOverrideReason(n.getOverrideReason());
    d.setDecisionReason(n.getDecisionReason());
    d.setCompletedAt(n.getCompletedAt());
    d.setConfirmedByName(
      n.getConfirmedBy() == null ? null : n.getConfirmedBy().getFullName()
    );
    d.setExportType(n.getExportType());
    return d;
  }
}
