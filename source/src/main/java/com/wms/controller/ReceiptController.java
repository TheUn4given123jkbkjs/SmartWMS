package com.wms.controller;

import com.wms.dto.receipt.ReceiptApprovalDTO;
import com.wms.dto.receipt.ReceiptNoteCreateDTO;
import com.wms.dto.receipt.ReceiptNoteResponseDTO;
import com.wms.service.ReceiptService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        @RequestHeader(value = "X-User-Id", defaultValue = "1") Long currentUserId
    ) {
        ReceiptNoteResponseDTO created = receiptService.createReceiptNote(dto, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/api/notes/{id}/approve")
    @ResponseBody
    public ResponseEntity<ReceiptNoteResponseDTO> approveReceiptNoteApi(
        @PathVariable Long id,
        @Valid @RequestBody ReceiptApprovalDTO approvalDTO,
        @RequestHeader(value = "X-User-Id", defaultValue = "2") Long currentUserId
    ) {
        ReceiptNoteResponseDTO approved = receiptService.approveReceiptNote(id, approvalDTO, currentUserId);
        return ResponseEntity.ok(approved);
    }
}
