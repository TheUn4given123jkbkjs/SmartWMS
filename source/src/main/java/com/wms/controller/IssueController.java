package com.wms.controller;

import com.wms.dto.issue.IssueNoteResponseDTO;
import com.wms.dto.issue.IssueRequestDTO;
import com.wms.dto.issue.PickListProposalDTO;
import com.wms.service.IssueService;
import com.wms.util.DispatchStrategy;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller Lập phiếu & Phê duyệt Xuất kho theo thuật toán FEFO/FIFO (UC04, UC05 / SCR05).
 */
@Controller
@RequestMapping("/issue")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    /**
     * Màn hình Lập lệnh xuất kho.
     */
    @GetMapping("/create")
    public String createIssueView(Model model) {
        return "issue/create";
    }

    /**
     * Màn hình Bốc hàng theo danh sách Pick List.
     */
    @GetMapping("/pick")
    public String pickListView(Model model) {
        return "issue/pick";
    }

    // --- REST APIs ---

    @GetMapping("/api/propose-fefo")
    @ResponseBody
    public ResponseEntity<List<PickListProposalDTO>> proposeAllocationApi(
        @RequestParam Long productId,
        @RequestParam BigDecimal requestedQty,
        @RequestParam(defaultValue = "FEFO") DispatchStrategy strategy
    ) {
        return ResponseEntity.ok(issueService.proposeAllocation(productId, requestedQty, strategy));
    }

    @PostMapping("/api/notes")
    @ResponseBody
    public ResponseEntity<IssueNoteResponseDTO> createIssueNoteApi(
        @Valid @RequestBody IssueRequestDTO dto,
        @RequestHeader(value = "X-User-Id", defaultValue = "1") Long currentUserId
    ) {
        IssueNoteResponseDTO created = issueService.createIssueNote(dto, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/api/notes/{id}/approve")
    @ResponseBody
    public ResponseEntity<IssueNoteResponseDTO> approveIssueNoteApi(
        @PathVariable Long id,
        @RequestHeader(value = "X-User-Id", defaultValue = "2") Long currentUserId
    ) {
        IssueNoteResponseDTO approved = issueService.approveIssueNote(id, currentUserId);
        return ResponseEntity.ok(approved);
    }

    @PostMapping("/api/notes/{id}/dispatch")
    @ResponseBody
    public ResponseEntity<IssueNoteResponseDTO> confirmDispatchApi(
        @PathVariable Long id,
        @RequestHeader(value = "X-User-Id", defaultValue = "1") Long currentUserId
    ) {
        IssueNoteResponseDTO dispatched = issueService.confirmDispatch(id, currentUserId);
        return ResponseEntity.ok(dispatched);
    }
}
