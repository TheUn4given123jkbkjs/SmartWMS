package com.wms.service.impl;

import com.wms.dto.receipt.ReceiptApprovalDTO;
import com.wms.dto.receipt.ReceiptNoteCreateDTO;
import com.wms.dto.receipt.ReceiptNoteResponseDTO;
import com.wms.entity.*;
import com.wms.exception.ResourceNotFoundException;
import com.wms.exception.SecurityViolationException;
import com.wms.repository.*;
import com.wms.service.ReceiptService;
import com.wms.util.ReceiptStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hiện thực Service Quản lý Nhập kho & Phê duyệt Nhập hàng (ACID Transaction).
 */
@Service
@Transactional
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptNoteRepository receiptNoteRepository;
    private final ReceiptNoteItemRepository receiptNoteItemRepository;
    private final ProductRepository productRepository;
    private final WarehouseLocationRepository locationRepository;
    private final StockBatchRepository stockBatchRepository;
    private final StockCardRepository stockCardRepository;
    private final UserRepository userRepository;

    public ReceiptServiceImpl(ReceiptNoteRepository receiptNoteRepository,
                              ReceiptNoteItemRepository receiptNoteItemRepository,
                              ProductRepository productRepository,
                              WarehouseLocationRepository locationRepository,
                              StockBatchRepository stockBatchRepository,
                              StockCardRepository stockCardRepository,
                              UserRepository userRepository) {
        this.receiptNoteRepository = receiptNoteRepository;
        this.receiptNoteItemRepository = receiptNoteItemRepository;
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
        this.stockBatchRepository = stockBatchRepository;
        this.stockCardRepository = stockCardRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ReceiptNoteResponseDTO createReceiptNote(ReceiptNoteCreateDTO dto, Long currentUserId) {
        User maker = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người lập phiếu ID: " + currentUserId));

        ReceiptNote note = new ReceiptNote();
        note.setReceiptNumber("RN-" + System.currentTimeMillis());
        note.setPoNumber(dto.getPoNumber());
        note.setMaker(maker);
        note.setStatus(ReceiptStatus.PENDING_APPROVAL);

        ReceiptNote saved = receiptNoteRepository.save(note);
        return mapToResponseDTO(saved);
    }

    @Override
    public ReceiptNoteResponseDTO approveReceiptNote(Long receiptId, ReceiptApprovalDTO approvalDTO, Long currentUserId) {
        ReceiptNote note = receiptNoteRepository.findById(receiptId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu nhập kho ID: " + receiptId));

        // Kiểm tra nguyên tắc phân tách trách nhiệm (người duyệt không trùng người lập)
        if (note.getMaker().getId().equals(currentUserId)) {
            throw new SecurityViolationException("Người lập phiếu không được phép tự phê duyệt phiếu nhập kho.");
        }

        User checker = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người phê duyệt ID: " + currentUserId));

        note.setChecker(checker);
        note.setStatus(ReceiptStatus.APPROVED);
        note.setUpdatedAt(LocalDateTime.now());

        ReceiptNote approved = receiptNoteRepository.save(note);
        return mapToResponseDTO(approved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReceiptNoteResponseDTO> getReceiptNotes(String status, String poNumber) {
        List<ReceiptNote> notes;
        if (status != null && !status.isBlank()) {
            notes = receiptNoteRepository.findByStatus(ReceiptStatus.valueOf(status));
        } else if (poNumber != null && !poNumber.isBlank()) {
            notes = receiptNoteRepository.findByPoNumber(poNumber);
        } else {
            notes = receiptNoteRepository.findAll();
        }

        return notes.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReceiptNoteResponseDTO getReceiptNoteById(Long receiptId) {
        ReceiptNote note = receiptNoteRepository.findById(receiptId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu nhập kho ID: " + receiptId));
        return mapToResponseDTO(note);
    }

    private ReceiptNoteResponseDTO mapToResponseDTO(ReceiptNote note) {
        ReceiptNoteResponseDTO dto = new ReceiptNoteResponseDTO();
        dto.setId(note.getId());
        dto.setReceiptCode(note.getReceiptNumber());
        dto.setPoNumber(note.getPoNumber());
        dto.setMakerName(note.getMaker() != null ? note.getMaker().getFullName() : null);
        dto.setCheckerName(note.getChecker() != null ? note.getChecker().getFullName() : null);
        dto.setStatus(note.getStatus());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setItems(new ArrayList<>());
        return dto;
    }
}
