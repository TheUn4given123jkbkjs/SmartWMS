package com.wms.repository;

import com.wms.entity.IssueNoteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IssueNoteItemRepository extends JpaRepository<IssueNoteItem, Long> {
    List<IssueNoteItem> findByIssueNoteId(Long issueId);
}
