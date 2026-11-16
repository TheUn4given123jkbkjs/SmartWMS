package com.wms.controller;

import com.wms.dto.inventory.AvailableStockDTO;
import com.wms.dto.inventory.NearExpiryAlertDTO;
import com.wms.dto.inventory.StockCardDTO;
import com.wms.service.InventoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller Quản lý Thẻ kho & Cảnh báo Tồn kho thời gian thực (UC06, UC07 / SCR02, SCR06).
 */
@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Màn hình Tra cứu Sổ thẻ kho điện tử (SCR06).
     */
    @GetMapping("/stock-card")
    public String stockCardView(Model model) {
        return "inventory/stock_card";
    }

    /**
     * Màn hình Bảng Cảnh báo Tồn kho & Cận hạn.
     */
    @GetMapping("/alerts")
    public String alertsView(Model model) {
        model.addAttribute("lowStocks", inventoryService.getLowStockAlerts());
        model.addAttribute("nearExpiries", inventoryService.getNearExpiryAlerts(30));
        return "inventory/alerts";
    }

    // --- REST APIs ---

    @GetMapping("/api/stock-cards")
    @ResponseBody
    public ResponseEntity<List<StockCardDTO>> getStockCardHistoryApi(
        @RequestParam Long productId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        return ResponseEntity.ok(inventoryService.getStockCardHistory(productId, fromDate, toDate));
    }

    @GetMapping("/api/available-stock")
    @ResponseBody
    public ResponseEntity<List<AvailableStockDTO>> getAllAvailableStockApi() {
        return ResponseEntity.ok(inventoryService.getAllAvailableStock());
    }

    @GetMapping("/api/alerts/low-stock")
    @ResponseBody
    public ResponseEntity<List<AvailableStockDTO>> getLowStockAlertsApi() {
        return ResponseEntity.ok(inventoryService.getLowStockAlerts());
    }

    @GetMapping("/api/alerts/near-expiry")
    @ResponseBody
    public ResponseEntity<List<NearExpiryAlertDTO>> getNearExpiryAlertsApi(
        @RequestParam(defaultValue = "30") int thresholdDays
    ) {
        return ResponseEntity.ok(inventoryService.getNearExpiryAlerts(thresholdDays));
    }
}
