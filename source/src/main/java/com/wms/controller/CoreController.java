package com.wms.controller;

import com.wms.entity.*;
import com.wms.exception.*;
import com.wms.repository.*;
import com.wms.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class CoreController {

  private final PurchaseOrderService orders;
  private final InventoryQueryService query;
  private final CatalogService catalog;

  public CoreController(
    PurchaseOrderService o,
    InventoryQueryService q,
    CatalogService c
  ) {
    orders = o;
    query = q;
    catalog = c;
  }

  @GetMapping("/receipt/api/purchase-orders")
  public List<PurchaseOrderService.View> orders() {
    return orders.list();
  }

  @PostMapping("/receipt/api/purchase-orders")
  public PurchaseOrderService.View importOrder(
    @Valid @RequestBody PurchaseOrderService.ImportRequest order,
    @SessionAttribute("user") com.wms.dto.auth.UserResponseDTO user
  ) {
    return orders.importApproved(order, user.getId());
  }

  @GetMapping("/inventory/api/batches")
  public List<InventoryQueryService.BatchView> batches(
    @RequestParam(required = false) String keyword,
    @RequestParam(required = false) Long categoryId,
    @RequestParam(required = false) String zone,
    @RequestParam(required = false) String status
  ) {
    return query.batches(keyword, categoryId, zone, status);
  }

  @GetMapping("/inventory/api/summary")
  public InventoryQueryService.Summary summary(
    @RequestParam Long productId,
    @RequestParam @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE
    ) LocalDate fromDate,
    @RequestParam @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE
    ) LocalDate toDate
  ) {
    return query.summary(productId, fromDate, toDate);
  }

  public record Limits(
    @NotNull @DecimalMin("0") @Digits(
      integer = 10,
      fraction = 2
    ) BigDecimal minStock,
    @NotNull @DecimalMin("0") @Digits(
      integer = 10,
      fraction = 2
    ) BigDecimal maxStock
  ) {}

  @PutMapping("/catalog/api/products/{id}/limits")
  public com.wms.dto.catalog.ProductResponseDTO limits(
    @PathVariable Long id,
    @Valid @RequestBody Limits limits
  ) {
    return catalog.updateLimits(id, limits.minStock(), limits.maxStock());
  }
}
