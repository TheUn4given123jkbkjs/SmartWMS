package com.wms.service.impl;

import com.wms.dto.inventory.*;
import com.wms.entity.*;
import com.wms.exception.*;
import com.wms.repository.*;
import com.wms.service.InventoryService;
import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

  private final ProductRepository products;
  private final StockBatchRepository batches;
  private final StockCardRepository cards;
  private final PurchaseOrderRepository orders;

  public InventoryServiceImpl(
    ProductRepository p,
    StockBatchRepository b,
    StockCardRepository c,
    PurchaseOrderRepository o
  ) {
    products = p;
    batches = b;
    cards = c;
    orders = o;
  }

  public BigDecimal calculateAvailableQuantity(Long id) {
    require(id);
    return batches
      .findByProductId(id)
      .stream()
      .filter(StockBatch::isEligible)
      .map(StockBatch::getAvailableQty)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private Product require(Long id) {
    return products
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("Không tìm thấy mặt hàng.")
      );
  }

  public List<AvailableStockDTO> getAllAvailableStock() {
    return products
      .findAll()
      .stream()
      .map(p -> {
        var list = batches.findByProductId(p.getId());
        BigDecimal hand = list
          .stream()
          .map(StockBatch::getOnHandQty)
          .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal reserved = list
          .stream()
          .map(StockBatch::getReservedQty)
          .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal available = calculateAvailableQuantity(p.getId());
        BigDecimal onOrder = orders
          .findAll()
          .stream()
          .filter(o -> "APPROVED".equals(o.getStatus()))
          .flatMap(o -> o.getItems().stream())
          .filter(i -> i.getProduct().getId().equals(p.getId()))
          .map(i -> i.getOrderedQty().subtract(i.getReceivedQty()))
          .reduce(BigDecimal.ZERO, BigDecimal::add);
        AvailableStockDTO dto = new AvailableStockDTO(
          p.getId(),
          p.getSkuCode(),
          p.getName(),
          p.getUom(),
          hand,
          reserved,
          available,
          p.getMinStock(),
          available.compareTo(p.getMinStock()) <= 0
        );
        dto.setMaxStock(p.getMaxStock());
        dto.setOnOrder(onOrder);
        dto.setSuggestedOrderQty(
          available.add(onOrder).compareTo(p.getMinStock()) <= 0
            ? p
              .getMaxStock()
              .subtract(available.add(onOrder))
              .max(BigDecimal.ZERO)
            : BigDecimal.ZERO
        );
        return dto;
      })
      .toList();
  }

  public List<StockCardDTO> getStockCardHistory(
    Long id,
    LocalDate from,
    LocalDate to
  ) {
    require(id);
    LocalDate start = from == null ? LocalDate.now().minusMonths(1) : from;
    LocalDate end = to == null ? LocalDate.now() : to;
    if (end.isBefore(start)) throw new InvalidQuantityException(
      "Đến ngày phải sau hoặc bằng từ ngày."
    );
    return cards
      .findByProductIdAndTransactionDateBetweenOrderByTransactionDateAsc(
        id,
        start.atStartOfDay(),
        end.atTime(LocalTime.MAX)
      )
      .stream()
      .sorted(
        Comparator.comparing(StockCard::getTransactionDate).thenComparing(
          StockCard::getId
        )
      )
      .map(c -> {
        StockCardDTO d = new StockCardDTO();
        d.setId(c.getId());
        d.setTransactionTime(c.getTransactionDate());
        d.setChangeType(c.getChangeType());
        d.setReferenceCode(c.getReferenceCode());
        d.setSkuCode(c.getProduct().getSkuCode());
        d.setProductName(c.getProduct().getName());
        d.setBatchCode(c.getStockBatch().getLotNumber());
        d.setLocationCode(c.getStockBatch().getLocation().getLocationCode());
        d.setChangeQty(c.getQtyChanged());
        d.setAfterQty(c.getAfterChanged());
        d.setBeforeQty(c.getAfterChanged().subtract(c.getQtyChanged()));
        d.setPerformedByName(
          c.getPerformedBy() == null ? null : c.getPerformedBy().getFullName()
        );
        d.setApprovedByName(
          c.getApprovedBy() == null ? null : c.getApprovedBy().getFullName()
        );
        d.setReason(c.getReason());
        return d;
      })
      .toList();
  }

  public List<AvailableStockDTO> getLowStockAlerts() {
    return getAllAvailableStock()
      .stream()
      .filter(AvailableStockDTO::getUnderMin)
      .toList();
  }

  public List<NearExpiryAlertDTO> getNearExpiryAlerts(int days) {
    if (days < 1 || days > 365) throw new InvalidQuantityException(
      "Ngưỡng cận hạn phải từ 1 đến 365 ngày."
    );
    LocalDate today = LocalDate.now();
    return batches
      .findNearExpiryBatches(today.plusDays(days))
      .stream()
      .map(b -> {
        long remaining = ChronoUnit.DAYS.between(today, b.getExpDate());
        return new NearExpiryAlertDTO(
          b.getId(),
          b.getLotNumber(),
          b.getProduct().getSkuCode(),
          b.getProduct().getName(),
          b.getLocation().getLocationCode(),
          b.getExpDate(),
          remaining,
          b.getOnHandQty(),
          remaining < 0 ? "EXPIRED" : remaining <= 7 ? "CRITICAL" : "WARNING"
        );
      })
      .toList();
  }
}
