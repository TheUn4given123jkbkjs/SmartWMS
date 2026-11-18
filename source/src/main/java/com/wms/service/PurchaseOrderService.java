package com.wms.service;

import com.wms.dto.purchase.PurchaseOrderImportDTO;
import com.wms.dto.purchase.PurchaseOrderResponseDTO;
import com.wms.entity.PurchaseOrder;
import java.math.BigDecimal;
import java.util.List;

/**
 * Service Interface quản lý dữ liệu Đơn mua hàng PO (FR02, BR-INB-01).
 */
public interface PurchaseOrderService {

  /** Lấy danh sách tất cả các đơn mua hàng kèm tiến độ giao hàng chi tiết */
  List<PurchaseOrderResponseDTO> getAllPurchaseOrders();

  /** Lấy chi tiết một đơn mua hàng theo mã PO */
  PurchaseOrderResponseDTO getPurchaseOrderByNumber(String poNumber);

  /** Tiếp nhận/Import một đơn mua hàng đã duyệt vào hệ thống */
  PurchaseOrderResponseDTO importPurchaseOrder(PurchaseOrderImportDTO dto, Long actorId);

  /** Tra cứu Entity PO bắt buộc tồn tại */
  PurchaseOrder require(String poNumber);

  /** Tính tổng số lượng hàng đang nằm trên các phiếu nhập chờ duyệt (Pending) */
  BigDecimal getPendingQty(String poNumber, Long productId, Long excludeReceiptId);
}
