package com.wms.repository;

import com.wms.entity.ReceiptNote;
import com.wms.util.ReceiptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptNoteRepository extends JpaRepository<ReceiptNote, Long> {
    Optional<ReceiptNote> findByReceiptNumber(String receiptNumber);
    List<ReceiptNote> findByPoNumber(String poNumber);
    List<ReceiptNote> findByStatus(ReceiptStatus status);
}
