package com.wms.service;

import com.wms.dto.issue.IssueNoteResponseDTO;
import com.wms.dto.issue.IssueRequestDTO;
import com.wms.dto.issue.PickListProposalDTO;
import com.wms.util.DispatchStrategy;
import java.math.BigDecimal;
import java.util.List;

public interface IssueService {
  List<PickListProposalDTO> proposeAllocation(
    Long productId,
    BigDecimal requestedQty,
    DispatchStrategy strategy
  );
  IssueNoteResponseDTO createIssueNote(IssueRequestDTO dto, Long currentUserId);
  IssueNoteResponseDTO approveIssueNote(Long issueId, Long currentUserId);
  IssueNoteResponseDTO confirmDispatch(Long issueId, Long currentUserId);
  IssueNoteResponseDTO completeIssueNote(Long id, Long userId);
  IssueNoteResponseDTO rejectIssueNote(Long id, String reason, Long userId);
  void cancelIssueNote(Long issueId, String reason, Long currentUserId);
  List<IssueNoteResponseDTO> getIssueNotes(String status);
  IssueNoteResponseDTO getIssueNoteById(Long issueId);
}
