package com.wms.service.impl;

import com.wms.dto.purchase.PurchaseOrderImportDTO;
import com.wms.dto.purchase.PurchaseOrderItemDTO;
import com.wms.dto.purchase.PurchaseOrderResponseDTO;
import com.wms.entity.Product;
import com.wms.entity.PurchaseOrder;
import com.wms.entity.PurchaseOrderItem;
import com.wms.entity.User;
import com.wms.exception.ConflictException;
import com.wms.exception.InvalidQuantityException;
import com.wms.exception.ResourceNotFoundException;
import com.wms.exception.SecurityViolationException;
import com.wms.repository.ProductRepository;
import com.wms.repository.PurchaseOrderRepository;
import com.wms.repository.ReceiptNoteRepository;
import com.wms.service.PurchaseOrderService;
import com.wms.service.WarehouseOperations;
import com.wms.util.ReceiptStatus;
import com.wms.util.UserRole;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hiện thực Service quản lý Đơn mua hàng PO (chuẩn MVC phân tầng).
 */
@Service
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

  private final PurchaseOrderRepository orders;
  private final ProductRepository products;
  private final ReceiptNoteRepository receipts;
  private final WarehouseOperations ops;

  public PurchaseOrderServiceImpl(
    PurchaseOrderRepository orders,
    ProductRepository products,
    ReceiptNoteRepository receipts,
    WarehouseOperations ops
  ) {
    this.orders = orders;
    this.products = products;
    this.receipts = receipts;
    this.ops = ops;
  }

  @Override
  @Transactional(readOnly = true)
  public List<PurchaseOrderResponseDTO> getAllPurchaseOrders() {
    return orders.findAll().stream().map(this::mapToDTO).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public PurchaseOrderResponseDTO getPurchaseOrderByNumber(String poNumber) {
    return mapToDTO(require(poNumber));
  }

  @Override
  public PurchaseOrderResponseDTO importPurchaseOrder(PurchaseOrderImportDTO dto, Long actorId) {
    ImportRequest request = new ImportRequest(
      dto.getPoNumber(),
      dto.getSupplierName(),
      dto.getItems().stream()
        .map(item -> new ImportLine(item.getProductId(), item.getOrderedQty()))
        .toList()
    );
    return mapToDTO(saveApproved(request, actorId));
  }

  @Override
  @Transactional(readOnly = true)
  public PurchaseOrder require(String poNumber) {
    return orders
      .findByPoNumber(poNumber)
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn mua hàng (PO) có mã: " + poNumber));
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getPendingQty(String poNumber, Long productId, Long excludeReceiptId) {
    return receipts
      .findAll()
      .stream()
      .filter(
        r ->
          r.getPoNumber() != null &&
          r.getPoNumber().equals(poNumber) &&
          r.getStatus() == ReceiptStatus.PENDING_APPROVAL &&
          (excludeReceiptId == null || !r.getId().equals(excludeReceiptId))
      )
      .flatMap(r -> r.getItems().stream())
      .filter(i -> i.getProduct() != null && i.getProduct().getId().equals(productId))
      .map(i -> i.getAcceptedQty() != null ? i.getAcceptedQty() : BigDecimal.ZERO)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private PurchaseOrderResponseDTO mapToDTO(PurchaseOrder po) {
    List<PurchaseOrderItemDTO> itemDTOs = po
      .getItems()
      .stream()
      .map(i -> {
        BigDecimal pending = getPendingQty(po.getPoNumber(), i.getProduct().getId(), null);
        BigDecimal remaining = i.getOrderedQty().subtract(i.getReceivedQty()).subtract(pending);
        if (remaining.signum() < 0) {
          remaining = BigDecimal.ZERO;
        }

        return new PurchaseOrderItemDTO(
          i.getProduct().getId(),
          i.getProduct().getSkuCode(),
          i.getProduct().getName(),
          i.getProduct().getUom(),
          i.getOrderedQty(),
          i.getReceivedQty(),
          pending,
          remaining
        );
      })
      .toList();

    return new PurchaseOrderResponseDTO(
      po.getId(),
      po.getPoNumber(),
      po.getSupplierName(),
      po.getStatus(),
      po.getCreatedAt(),
      itemDTOs
    );
  }

  @Override
  @Transactional(readOnly = true)
  public List<View> list() {
    return orders.findAll().stream().map(this::view).toList();
  }

  @Override
  public View importApproved(ImportRequest request, Long actorId) {
    return view(saveApproved(request, actorId));
  }

  private PurchaseOrder saveApproved(ImportRequest request, Long actorId) {
    if (actorId == null) {
      throw new SecurityViolationException("Phải đăng nhập để nhập PO.");
    }
    User actor = ops.actor(actorId, UserRole.ROLE_MANAGER);
    ops.lock();
    String poNumber = request.poNumber().trim();
    if (orders.findByPoNumber(poNumber).isPresent()) {
      throw new ConflictException("Mã PO đã tồn tại.");
    }
    PurchaseOrder po = new PurchaseOrder();
    po.setPoNumber(poNumber);
    po.setSupplierName(request.supplierName().trim());
    po.setStatus("APPROVED");

    Set<Long> seen = new HashSet<>();
    for (var line : request.items()) {
      if (!seen.add(line.productId())) {
        throw new InvalidQuantityException("PO không được trùng SKU.");
      }
      Product p = products
        .findById(line.productId())
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy SKU."));
      PurchaseOrderItem i = new PurchaseOrderItem();
      i.setPurchaseOrder(po);
      i.setProduct(p);
      i.setOrderedQty(ops.qty(line.orderedQty(), true));
      i.setReceivedQty(BigDecimal.ZERO);
      po.getItems().add(i);
    }
    PurchaseOrder saved = orders.save(po);
    ops.audit(saved.getPoNumber(), "IMPORT_APPROVED_PO", actor);
    return saved;
  }

  @Override
  public BigDecimal pending(String number, Long productId, Long excludeReceipt) {
    return getPendingQty(number, productId, excludeReceipt);
  }

  @Override
  public View view(PurchaseOrder po) {
    return new View(
      po.getId(),
      po.getPoNumber(),
      po.getSupplierName(),
      po.getStatus(),
      po.getItems().stream().map(i -> {
        BigDecimal pending = pending(po.getPoNumber(), i.getProduct().getId(), null);
        BigDecimal remaining = i.getOrderedQty().subtract(i.getReceivedQty()).subtract(pending);
        if (remaining.signum() < 0) remaining = BigDecimal.ZERO;
        return new Line(
          i.getProduct().getId(),
          i.getProduct().getSkuCode(),
          i.getProduct().getName(),
          i.getOrderedQty(),
          i.getReceivedQty(),
          pending,
          remaining
        );
      }).toList()
    );
  }
}
