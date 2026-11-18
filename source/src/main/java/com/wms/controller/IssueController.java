package com.wms.controller;

import com.wms.dto.auth.UserResponseDTO;
import com.wms.dto.issue.IssueNoteResponseDTO;
import com.wms.dto.issue.IssueRequestDTO;
import com.wms.dto.issue.PickListProposalDTO;
import com.wms.service.IssueService;
import com.wms.util.DispatchStrategy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    return ResponseEntity.ok(
      issueService.proposeAllocation(productId, requestedQty, strategy)
    );
  }

  @PostMapping("/api/notes")
  @ResponseBody
  public ResponseEntity<IssueNoteResponseDTO> createIssueNoteApi(
    @Valid @RequestBody IssueRequestDTO dto,
    @SessionAttribute(value = "user", required = false) UserResponseDTO user,
    @RequestHeader(value = "X-User-Id", defaultValue = "1") Long currentUserId
  ) {
    Long actorId = user != null ? user.getId() : currentUserId;
    IssueNoteResponseDTO created = issueService.createIssueNote(dto, actorId);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PostMapping("/api/notes/{id}/approve")
  @ResponseBody
  public ResponseEntity<IssueNoteResponseDTO> approveIssueNoteApi(
    @PathVariable Long id,
    @SessionAttribute(value = "user", required = false) UserResponseDTO user,
    @RequestHeader(value = "X-User-Id", defaultValue = "2") Long currentUserId
  ) {
    Long actorId = user != null ? user.getId() : currentUserId;
    IssueNoteResponseDTO approved = issueService.approveIssueNote(id, actorId);
    return ResponseEntity.ok(approved);
  }

  @PostMapping("/api/notes/{id}/dispatch")
  @ResponseBody
  public ResponseEntity<IssueNoteResponseDTO> confirmDispatchApi(
    @PathVariable Long id,
    @SessionAttribute(value = "user", required = false) UserResponseDTO user,
    @RequestHeader(value = "X-User-Id", defaultValue = "1") Long currentUserId
  ) {
    Long actorId = user != null ? user.getId() : currentUserId;
    IssueNoteResponseDTO dispatched = issueService.confirmDispatch(id, actorId);
    return ResponseEntity.ok(dispatched);
  }

  public record Decision(
    @NotBlank @Size(max = 255) String reason
  ) {}

  @PostMapping("/api/notes/{id}/complete")
  @ResponseBody
  public IssueNoteResponseDTO complete(
    @PathVariable Long id,
    @SessionAttribute(value = "user", required = false) UserResponseDTO user,
    @RequestHeader(value = "X-User-Id", defaultValue = "1") Long currentUserId
  ) {
    Long actorId = user != null ? user.getId() : currentUserId;
    return issueService.completeIssueNote(id, actorId);
  }

  @PostMapping("/api/notes/{id}/reject")
  @ResponseBody
  public IssueNoteResponseDTO reject(
    @PathVariable Long id,
    @Valid @RequestBody Decision decision,
    @SessionAttribute(value = "user", required = false) UserResponseDTO user,
    @RequestHeader(value = "X-User-Id", defaultValue = "2") Long currentUserId
  ) {
    Long actorId = user != null ? user.getId() : currentUserId;
    return issueService.rejectIssueNote(id, decision.reason(), actorId);
  }

  @PostMapping("/api/notes/{id}/cancel")
  @ResponseBody
  public ResponseEntity<Void> cancel(
    @PathVariable Long id,
    @Valid @RequestBody Decision decision,
    @SessionAttribute(value = "user", required = false) UserResponseDTO user,
    @RequestHeader(value = "X-User-Id", defaultValue = "1") Long currentUserId
  ) {
    Long actorId = user != null ? user.getId() : currentUserId;
    issueService.cancelIssueNote(id, decision.reason(), actorId);
    return ResponseEntity.noContent().build();
  }
}
