package com.wms.controller;

import com.wms.dto.receipt.ReceiptApprovalDTO;
import com.wms.dto.receipt.ReceiptNoteCreateDTO;
import com.wms.dto.receipt.ReceiptNoteResponseDTO;
import com.wms.service.ReceiptService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller Lập phiếu & Phê duyệt Nhập kho (UC02, UC03 / SCR04).
 */
@Controller
@RequestMapping("/receipt")
public class ReceiptController {

  private final ReceiptService receiptService;

  public ReceiptController(ReceiptService receiptService) {
    this.receiptService = receiptService;
  }

  /**
   * Màn hình Lập phiếu nhập kho PO.
   */
  @GetMapping("/create")
  public String createReceiptView(Model model) {
    return "receipt/create";
  }

  /**
   * Màn hình Phê duyệt nhập kho (Dành cho Quản lý kho).
   */
  @GetMapping("/approve")
  public String approveReceiptView(Model model) {
    return "receipt/approve";
  }

  // --- REST APIs ---

  @GetMapping("/api/notes")
  @ResponseBody
  public ResponseEntity<List<ReceiptNoteResponseDTO>> getReceiptNotesApi(
    @RequestParam(required = false) String status,
    @RequestParam(required = false) String poNumber
  ) {
    return ResponseEntity.ok(receiptService.getReceiptNotes(status, poNumber));
  }

  @PostMapping("/api/notes")
  @ResponseBody
  public ResponseEntity<ReceiptNoteResponseDTO> createReceiptNoteApi(
    @Valid @RequestBody ReceiptNoteCreateDTO dto,
    @SessionAttribute("user") com.wms.dto.auth.UserResponseDTO user
  ) {
    ReceiptNoteResponseDTO created = receiptService.createReceiptNote(
      dto,
      user.getId()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PostMapping("/api/notes/{id}/approve")
  @ResponseBody
  public ResponseEntity<ReceiptNoteResponseDTO> approveReceiptNoteApi(
    @PathVariable Long id,
    @Valid @RequestBody ReceiptApprovalDTO approvalDTO,
    @SessionAttribute("user") com.wms.dto.auth.UserResponseDTO user
  ) {
    ReceiptNoteResponseDTO approved = receiptService.approveReceiptNote(
      id,
      approvalDTO,
      user.getId()
    );
    return ResponseEntity.ok(approved);
  }

  public record Decision(
    @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(max = 255) String reason
  ) {}

  @PostMapping("/api/notes/{id}/cancel")
  @ResponseBody
  public ResponseEntity<Void> cancelReceiptNoteApi(
    @PathVariable Long id,
    @Valid @RequestBody Decision decision,
    @SessionAttribute(value = "user", required = false) com.wms.dto.auth.UserResponseDTO user,
    @RequestHeader(value = "X-User-Id", defaultValue = "1") Long currentUserId
  ) {
    Long actorId = user != null ? user.getId() : currentUserId;
    receiptService.cancelReceiptNote(id, decision.reason(), actorId);
    return ResponseEntity.noContent().build();
  }
}
