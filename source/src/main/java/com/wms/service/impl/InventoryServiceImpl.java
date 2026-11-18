package com.wms.service.impl;

import com.wms.dto.inventory.AvailableStockDTO;
import com.wms.dto.inventory.NearExpiryAlertDTO;
import com.wms.dto.inventory.StockCardDTO;
import com.wms.entity.Product;
import com.wms.entity.StockBatch;
import com.wms.entity.StockCard;
import com.wms.repository.ProductRepository;
import com.wms.repository.StockBatchRepository;
import com.wms.repository.StockCardRepository;
import com.wms.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hiện thực Service Quản lý Tồn kho, Sổ thẻ kho & Cảnh báo tồn min / cận hạn.
 */
@Service
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final StockBatchRepository stockBatchRepository;
    private final StockCardRepository stockCardRepository;

    public InventoryServiceImpl(ProductRepository productRepository,
                                StockBatchRepository stockBatchRepository,
                                StockCardRepository stockCardRepository) {
        this.productRepository = productRepository;
        this.stockBatchRepository = stockBatchRepository;
        this.stockCardRepository = stockCardRepository;
    }

    @Override
    public BigDecimal calculateAvailableQuantity(Long productId) {
        List<StockBatch> batches = stockBatchRepository.findByProductId(productId);
        int totalAvailable = batches.stream()
            .mapToInt(StockBatch::getAvailableQty)
            .sum();
        return BigDecimal.valueOf(totalAvailable);
    }

    @Override
    public List<AvailableStockDTO> getAllAvailableStock() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(p -> {
            List<StockBatch> batches = stockBatchRepository.findByProductId(p.getId());
            int totalOnHand = batches.stream().mapToInt(StockBatch::getOnHandQty).sum();
            int totalReserved = batches.stream().mapToInt(StockBatch::getReservedQty).sum();
            int available = batches.stream().mapToInt(StockBatch::getAvailableQty).sum();
            boolean underMin = available < p.getMinStock();

            return new AvailableStockDTO(
                p.getId(), p.getSkuCode(), p.getName(), p.getUom(),
                BigDecimal.valueOf(totalOnHand),
                BigDecimal.valueOf(totalReserved),
                BigDecimal.valueOf(available),
                BigDecimal.valueOf(p.getMinStock()),
                underMin
            );
        }).collect(Collectors.toList());
    }

    @Override
    public List<StockCardDTO> getStockCardHistory(Long productId, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime start = fromDate != null ? fromDate.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = toDate != null ? toDate.atTime(LocalTime.MAX) : LocalDateTime.now();

        List<StockCard> cards = stockCardRepository.findByProductIdAndTransactionDateBetweenOrderByTransactionDateAsc(
            productId, start, end
        );

        return cards.stream().map(c -> {
            StockCardDTO dto = new StockCardDTO();
            dto.setId(c.getId());
            dto.setTransactionTime(c.getTransactionDate());
            dto.setChangeType(c.getChangeType());
            dto.setReferenceCode(c.getReferenceCode());
            dto.setSkuCode(c.getProduct().getSkuCode());
            dto.setProductName(c.getProduct().getName());
            dto.setBatchCode(c.getStockBatch() != null ? c.getStockBatch().getLotNumber() : null);
            dto.setLocationCode(c.getStockBatch() != null && c.getStockBatch().getLocation() != null ? c.getStockBatch().getLocation().getLocationCode() : null);
            dto.setChangeQty(BigDecimal.valueOf(c.getQtyChanged()));
            dto.setAfterQty(BigDecimal.valueOf(c.getAfterChanged()));
            dto.setBeforeQty(BigDecimal.valueOf(c.getAfterChanged() - c.getQtyChanged()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AvailableStockDTO> getLowStockAlerts() {
        return getAllAvailableStock().stream()
            .filter(AvailableStockDTO::getUnderMin)
            .collect(Collectors.toList());
    }

    @Override
    public List<NearExpiryAlertDTO> getNearExpiryAlerts(int thresholdDays) {
        LocalDate threshold = LocalDate.now().plusDays(thresholdDays);
        List<StockBatch> batches = stockBatchRepository.findNearExpiryBatches(threshold);

        return batches.stream().map(b -> {
            long days = ChronoUnit.DAYS.between(LocalDate.now(), b.getExpDate());
            String level = days <= 0 ? "EXPIRED" : (days <= 15 ? "CRITICAL" : "WARNING");

            return new NearExpiryAlertDTO(
                b.getId(), b.getLotNumber(),
                b.getProduct().getSkuCode(), b.getProduct().getName(),
                b.getLocation().getLocationCode(),
                b.getExpDate(), days,
                BigDecimal.valueOf(b.getOnHandQty()),
                level
            );
        }).collect(Collectors.toList());
    }
}
