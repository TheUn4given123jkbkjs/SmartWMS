package com.wms.repository;

import com.wms.entity.IssueNote;
import com.wms.util.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IssueNoteRepository extends JpaRepository<IssueNote, Long> {
    Optional<IssueNote> findByIssueNumber(String issueNumber);
    List<IssueNote> findByStatus(IssueStatus status);
}
