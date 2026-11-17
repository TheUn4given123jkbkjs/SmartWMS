package com.wms.service;

import com.wms.entity.*;
import com.wms.exception.*;
import com.wms.repository.*;
import com.wms.util.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PurchaseOrderService {

  private final PurchaseOrderRepository orders;
  private final ProductRepository products;
  private final ReceiptNoteRepository receipts;
  private final WarehouseOperations ops;

  public PurchaseOrderService(
    PurchaseOrderRepository o,
    ProductRepository p,
    ReceiptNoteRepository r,
    WarehouseOperations ops
  ) {
    orders = o;
    products = p;
    receipts = r;
    this.ops = ops;
  }

  public record Line(
    Long productId,
    String skuCode,
    String productName,
    BigDecimal orderedQty,
    BigDecimal receivedQty,
    BigDecimal pendingQty,
    BigDecimal remainingQty
  ) {}

  public record View(
    Long id,
    String poNumber,
    String supplierName,
    String status,
    List<Line> items
  ) {}

  public record ImportLine(
    @NotNull Long productId,
    @NotNull @DecimalMin(value = "0", inclusive = false) BigDecimal orderedQty
  ) {}

  public record ImportRequest(
    @NotBlank @Size(max = 50) String poNumber,
    @NotBlank @Size(max = 255) String supplierName,
    @NotEmpty List<@Valid ImportLine> items
  ) {}

  @Transactional(readOnly = true)
  public List<View> list() {
    return orders.findAll().stream().map(this::view).toList();
  }

  public View importApproved(ImportRequest request, Long actor) {
    ops.lock();
    ops.actor(actor, UserRole.ROLE_MANAGER);
    if (
      orders.findByPoNumber(request.poNumber()).isPresent()
    ) throw new ConflictException("Mã PO đã tồn tại.");
    PurchaseOrder po = new PurchaseOrder();
    po.setPoNumber(request.poNumber());
    po.setSupplierName(request.supplierName());
    Set<Long> seen = new HashSet<>();
    for (var line : request.items()) {
      if (!seen.add(line.productId())) throw new InvalidQuantityException(
        "PO không được trùng SKU."
      );
      Product p = products
        .findById(line.productId())
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy SKU.")
        );
      PurchaseOrderItem i = new PurchaseOrderItem();
      i.setPurchaseOrder(po);
      i.setProduct(p);
      i.setOrderedQty(ops.qty(line.orderedQty(), true));
      po.getItems().add(i);
    }
    orders.save(po);
    ops.audit(
      po.getPoNumber(),
      "IMPORT_APPROVED_PO",
      ops.actor(actor, UserRole.ROLE_MANAGER)
    );
    return view(po);
  }

  public BigDecimal pending(
    String number,
    Long productId,
    Long excludeReceipt
  ) {
    return receipts
      .findAll()
      .stream()
      .filter(
        r ->
          r.getPoNumber().equals(number) &&
          r.getStatus() == ReceiptStatus.PENDING_APPROVAL &&
          (excludeReceipt == null || !r.getId().equals(excludeReceipt))
      )
      .flatMap(r -> r.getItems().stream())
      .filter(i -> i.getProduct().getId().equals(productId))
      .map(ReceiptNoteItem::getAcceptedQty)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public PurchaseOrder require(String number) {
    return orders
      .findByPoNumber(number)
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy PO."));
  }

  public View view(PurchaseOrder po) {
    return new View(
      po.getId(),
      po.getPoNumber(),
      po.getSupplierName(),
      po.getStatus(),
      po
        .getItems()
        .stream()
        .map(i -> {
          BigDecimal pending = pending(
            po.getPoNumber(),
            i.getProduct().getId(),
            null
          );
          return new Line(
            i.getProduct().getId(),
            i.getProduct().getSkuCode(),
            i.getProduct().getName(),
            i.getOrderedQty(),
            i.getReceivedQty(),
            pending,
            i.getOrderedQty().subtract(i.getReceivedQty()).subtract(pending)
          );
        })
        .toList()
    );
  }
}
