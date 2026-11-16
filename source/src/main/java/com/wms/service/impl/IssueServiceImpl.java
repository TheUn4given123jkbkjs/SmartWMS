package com.wms.service.impl;

import com.wms.dto.issue.IssueNoteResponseDTO;
import com.wms.dto.issue.IssueRequestDTO;
import com.wms.dto.issue.PickListProposalDTO;
import com.wms.entity.*;
import com.wms.exception.InsufficientStockException;
import com.wms.exception.ResourceNotFoundException;
import com.wms.exception.SecurityViolationException;
import com.wms.repository.*;
import com.wms.service.IssueService;
import com.wms.util.DispatchStrategy;
import com.wms.util.IssueStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hiện thực Service Quản lý Xuất kho & Thuật toán Phân bổ FEFO/FIFO (ACID Transaction).
 */
@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private final IssueNoteRepository issueNoteRepository;
    private final IssueNoteItemRepository issueNoteItemRepository;
    private final ProductRepository productRepository;
    private final StockBatchRepository stockBatchRepository;
    private final StockCardRepository stockCardRepository;
    private final UserRepository userRepository;

    public IssueServiceImpl(IssueNoteRepository issueNoteRepository,
                            IssueNoteItemRepository issueNoteItemRepository,
                            ProductRepository productRepository,
                            StockBatchRepository stockBatchRepository,
                            StockCardRepository stockCardRepository,
                            UserRepository userRepository) {
        this.issueNoteRepository = issueNoteRepository;
        this.issueNoteItemRepository = issueNoteItemRepository;
        this.productRepository = productRepository;
        this.stockBatchRepository = stockBatchRepository;
        this.stockCardRepository = stockCardRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PickListProposalDTO> proposeAllocation(Long productId, BigDecimal requestedQty, DispatchStrategy strategy) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm ID: " + productId));

        List<StockBatch> batches = stockBatchRepository.findAvailableBatchesOrderByFefo(productId, LocalDate.now());
        List<PickListProposalDTO> proposals = new ArrayList<>();
        BigDecimal remaining = requestedQty;

        for (StockBatch batch : batches) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
            BigDecimal available = BigDecimal.valueOf(batch.getAvailableQty());
            BigDecimal pick = remaining.min(available);

            proposals.add(new PickListProposalDTO(
                product.getId(), product.getSkuCode(), product.getName(),
                batch.getId(), batch.getLotNumber(), batch.getLocation().getLocationCode(),
                batch.getExpDate(), pick
            ));
            remaining = remaining.subtract(pick);
        }

        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            throw new InsufficientStockException("Tồn kho khả dụng không đủ để xuất số lượng yêu cầu. Còn thiếu: " + remaining);
        }

        return proposals;
    }

    @Override
    public IssueNoteResponseDTO createIssueNote(IssueRequestDTO dto, Long currentUserId) {
        User maker = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người lập phiếu ID: " + currentUserId));

        IssueNote note = new IssueNote();
        note.setIssueNumber("IN-" + System.currentTimeMillis());
        note.setDispatchStrategy(dto.getStrategy() != null ? dto.getStrategy() : DispatchStrategy.FEFO);
        note.setMaker(maker);
        note.setStatus(IssueStatus.PENDING_APPROVAL);

        IssueNote saved = issueNoteRepository.save(note);
        return mapToResponseDTO(saved);
    }

    @Override
    public IssueNoteResponseDTO approveIssueNote(Long issueId, Long currentUserId) {
        IssueNote note = issueNoteRepository.findById(issueId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu xuất kho ID: " + issueId));

        if (note.getMaker().getId().equals(currentUserId)) {
            throw new SecurityViolationException("Người lập phiếu không được phép tự phê duyệt phiếu xuất kho.");
        }

        User checker = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người phê duyệt ID: " + currentUserId));

        note.setChecker(checker);
        note.setStatus(IssueStatus.APPROVED_READY_TO_PICK);
        note.setUpdatedAt(LocalDateTime.now());

        IssueNote approved = issueNoteRepository.save(note);
        return mapToResponseDTO(approved);
    }

    @Override
    public void cancelIssueNote(Long issueId, String reason, Long currentUserId) {
        IssueNote note = issueNoteRepository.findById(issueId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu xuất kho ID: " + issueId));

        note.setStatus(IssueStatus.CANCELLED);
        note.setOverrideReason(reason);
        note.setUpdatedAt(LocalDateTime.now());
        issueNoteRepository.save(note);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssueNoteResponseDTO> getIssueNotes(String status) {
        List<IssueNote> notes;
        if (status != null && !status.isBlank()) {
            notes = issueNoteRepository.findByStatus(IssueStatus.valueOf(status));
        } else {
            notes = issueNoteRepository.findAll();
        }
        return notes.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public IssueNoteResponseDTO getIssueNoteById(Long issueId) {
        IssueNote note = issueNoteRepository.findById(issueId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu xuất kho ID: " + issueId));
        return mapToResponseDTO(note);
    }

    private IssueNoteResponseDTO mapToResponseDTO(IssueNote note) {
        IssueNoteResponseDTO dto = new IssueNoteResponseDTO();
        dto.setId(note.getId());
        dto.setIssueCode(note.getIssueNumber());
        dto.setStrategy(note.getDispatchStrategy());
        dto.setMakerName(note.getMaker() != null ? note.getMaker().getFullName() : null);
        dto.setCheckerName(note.getChecker() != null ? note.getChecker().getFullName() : null);
        dto.setStatus(note.getStatus());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setPickList(new ArrayList<>());
        return dto;
    }
}
