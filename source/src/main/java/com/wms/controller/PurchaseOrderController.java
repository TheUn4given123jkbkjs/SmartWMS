package com.wms.controller;

import com.wms.dto.auth.UserResponseDTO;
import com.wms.dto.purchase.PurchaseOrderImportDTO;
import com.wms.dto.purchase.PurchaseOrderResponseDTO;
import com.wms.service.PurchaseOrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller chuyên trách quản lý Đơn mua hàng PO (chuẩn MVC phân tầng - SRP).
 * Phục vụ đối soát số lượng và kiểm soát dòng hàng nhập (FR02, BR-INB-01).
 */
@RestController
@RequestMapping("/receipt/api/purchase-orders")
public class PurchaseOrderController {

  private final PurchaseOrderService purchaseOrderService;

  public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
    this.purchaseOrderService = purchaseOrderService;
  }

  /**
   * API lấy danh sách toàn bộ các Đơn mua hàng PO kèm số lượng còn lại chưa giao.
   */
  @GetMapping
  public ResponseEntity<List<PurchaseOrderResponseDTO>> getAllOrders() {
    return ResponseEntity.ok(purchaseOrderService.getAllPurchaseOrders());
  }

  /**
   * API lấy chi tiết một đơn mua hàng PO theo số hiệu PO.
   */
  @GetMapping("/{poNumber}")
  public ResponseEntity<PurchaseOrderResponseDTO> getOrderByNumber(@PathVariable String poNumber) {
    return ResponseEntity.ok(purchaseOrderService.getPurchaseOrderByNumber(poNumber));
  }

  /**
   * API tiếp nhận/Import đơn mua hàng PO đã được duyệt.
   */
  @PostMapping
  public ResponseEntity<PurchaseOrderResponseDTO> importOrder(
    @Valid @RequestBody PurchaseOrderImportDTO request,
    HttpSession session
  ) {
    UserResponseDTO currentUser = (UserResponseDTO) session.getAttribute("user");
    Long actorId = currentUser != null ? currentUser.getId() : 1L;

    PurchaseOrderResponseDTO result = purchaseOrderService.importPurchaseOrder(request, actorId);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }
}
