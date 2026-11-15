package com.wms.service;

import com.wms.dto.inventory.AvailableStockDTO;
import com.wms.dto.inventory.NearExpiryAlertDTO;
import com.wms.dto.inventory.StockCardDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InventoryService {
    BigDecimal calculateAvailableQuantity(Long productId);
    List<AvailableStockDTO> getAllAvailableStock();
    List<StockCardDTO> getStockCardHistory(Long productId, LocalDate fromDate, LocalDate toDate);
    List<AvailableStockDTO> getLowStockAlerts();
    List<NearExpiryAlertDTO> getNearExpiryAlerts(int thresholdDays);
}

