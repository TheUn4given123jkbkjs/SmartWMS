package com.wms.service;

import com.wms.dto.receipt.ReceiptApprovalDTO;
import com.wms.dto.receipt.ReceiptNoteCreateDTO;
import com.wms.dto.receipt.ReceiptNoteResponseDTO;

import java.util.List;

public interface ReceiptService {
    ReceiptNoteResponseDTO createReceiptNote(ReceiptNoteCreateDTO dto, Long currentUserId);
    ReceiptNoteResponseDTO approveReceiptNote(Long receiptId, ReceiptApprovalDTO approvalDTO, Long currentUserId);
    void cancelReceiptNote(Long receiptId, String reason, Long currentUserId);
    List<ReceiptNoteResponseDTO> getReceiptNotes(String status, String poNumber);
    ReceiptNoteResponseDTO getReceiptNoteById(Long receiptId);
}

