package com.wms.controller;

import com.wms.dto.issue.IssueNoteResponseDTO;
import com.wms.dto.receipt.ReceiptNoteResponseDTO;
import com.wms.service.IssueService;
import com.wms.service.ReceiptService;
import com.wms.service.UiQueryService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
public class UiQueryController {

  private final UiQueryService query;
  private final IssueService issues;
  private final ReceiptService receipts;

  public UiQueryController(
    UiQueryService query,
    IssueService issues,
    ReceiptService receipts
  ) {
    this.query = query;
    this.issues = issues;
    this.receipts = receipts;
  }

  @GetMapping("/catalog/api/categories")
  public List<UiQueryService.CategoryView> categories() {
    return query.categories();
  }

  @GetMapping("/catalog/api/locations")
  public List<UiQueryService.LocationView> locations() {
    return query.locations();
  }

  @GetMapping("/issue/api/notes")
  public List<IssueNoteResponseDTO> issues(
    @RequestParam(required = false) String status
  ) {
    return issues.getIssueNotes(status);
  }

  @GetMapping("/issue/api/notes/{id}")
  public IssueNoteResponseDTO issue(@PathVariable Long id) {
    return issues.getIssueNoteById(id);
  }

  @GetMapping("/receipt/api/notes/{id}")
  public ReceiptNoteResponseDTO receipt(@PathVariable Long id) {
    return receipts.getReceiptNoteById(id);
  }
}
