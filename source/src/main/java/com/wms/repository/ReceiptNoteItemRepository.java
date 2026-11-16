package com.wms.repository;

import com.wms.entity.ReceiptNoteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReceiptNoteItemRepository extends JpaRepository<ReceiptNoteItem, Long> {
    List<ReceiptNoteItem> findByReceiptNoteId(Long receiptId);
}
