package com.wms.service;

import com.wms.entity.*;
import com.wms.exception.*;
import com.wms.repository.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InventoryQueryService {

  private final StockBatchRepository batches;
  private final StockCardRepository cards;
  private final ProductRepository products;

  public InventoryQueryService(
    StockBatchRepository b,
    StockCardRepository c,
    ProductRepository p
  ) {
    batches = b;
    cards = c;
    products = p;
  }

  public record BatchView(
    Long batchId,
    Long productId,
    Long categoryId,
    String skuCode,
    String productName,
    String uom,
    String batchCode,
    LocalDate expiryDate,
    Long locationId,
    String locationCode,
    String zone,
    String status,
    BigDecimal onHandQty,
    BigDecimal reservedQty,
    BigDecimal availableQty
  ) {}

  public List<BatchView> batches(
    String keyword,
    Long categoryId,
    String zone,
    String status
  ) {
    if (zone != null && !zone.isBlank()) try {
      com.wms.util.ZoneType.valueOf(zone);
    } catch (Exception e) {
      throw new InvalidQuantityException("Khu vực không hợp lệ.");
    }
    if (
      status != null &&
      !status.isBlank() &&
      !List.of("IN_STOCK", "OUT_OF_STOCK", "EXPIRED", "DAMAGED").contains(
        status
      )
    ) throw new InvalidQuantityException("Trạng thái tồn không hợp lệ.");
    return batches
      .findAll()
      .stream()
      .filter(
        b ->
          categoryId == null ||
          b.getProduct().getCategory().getId().equals(categoryId)
      )
      .filter(
        b ->
          zone == null ||
          zone.isBlank() ||
          b.getLocation().getZone().name().equals(zone)
      )
      .map(b ->
        new BatchView(
          b.getId(),
          b.getProduct().getId(),
          b.getProduct().getCategory().getId(),
          b.getProduct().getSkuCode(),
          b.getProduct().getName(),
          b.getProduct().getUom(),
          b.getLotNumber(),
          b.getExpDate(),
          b.getLocation().getId(),
          b.getLocation().getLocationCode(),
          b.getLocation().getZone().name(),
          b.getExpDate() != null && b.getExpDate().isBefore(LocalDate.now())
            ? "EXPIRED"
            : b.getStatus(),
          b.getOnHandQty(),
          b.getReservedQty(),
          b.isEligible() ? b.getAvailableQty() : BigDecimal.ZERO
        )
      )
      .filter(
        b ->
          keyword == null ||
          (b.skuCode() +
            " " +
            b.productName() +
            " " +
            b.batchCode() +
            " " +
            b.locationCode()).toLowerCase()
            .contains(keyword.toLowerCase())
      )
      .filter(
        b ->
          status == null ||
          status.isBlank() ||
          (status.equals("IN_STOCK") && b.availableQty().signum() > 0) ||
          (status.equals("OUT_OF_STOCK") && b.availableQty().signum() == 0) ||
          status.equals(b.status())
      )
      .toList();
  }

  public record Summary(
    BigDecimal opening,
    BigDecimal received,
    BigDecimal issued,
    BigDecimal closing
  ) {}

  public Summary summary(Long id, LocalDate from, LocalDate to) {
    if (!products.existsById(id)) throw new ResourceNotFoundException(
      "Không tìm thấy mặt hàng."
    );
    if (to.isBefore(from)) throw new InvalidQuantityException(
      "Khoảng ngày không hợp lệ."
    );
    BigDecimal opening = BigDecimal.ZERO, in = BigDecimal.ZERO, out =
      BigDecimal.ZERO;
    for (StockCard c : cards.findByProductIdOrderByTransactionDateDesc(id)) {
      if (c.getTransactionDate().toLocalDate().isBefore(from)) opening =
        opening.add(c.getQtyChanged());
      else if (!c.getTransactionDate().toLocalDate().isAfter(to)) {
        if (c.getQtyChanged().signum() > 0) in = in.add(c.getQtyChanged());
        else out = out.subtract(c.getQtyChanged());
      }
    }
    return new Summary(opening, in, out, opening.add(in).subtract(out));
  }
}
