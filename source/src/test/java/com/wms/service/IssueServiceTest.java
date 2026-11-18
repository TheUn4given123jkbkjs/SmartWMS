package com.wms.service;

import com.wms.dto.issue.IssueNoteResponseDTO;
import com.wms.entity.*;
import com.wms.exception.ExpiredLotException;
import com.wms.repository.*;
import com.wms.service.impl.IssueServiceImpl;
import com.wms.util.IssueStatus;
import com.wms.util.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private IssueNoteRepository issueNoteRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockBatchRepository stockBatchRepository;

    @Mock
    private WarehouseOperations ops;

    @InjectMocks
    private IssueServiceImpl issueService;

    private User maker;
    private User checker;
    private Product product;
    private WarehouseLocation location;
    private StockBatch activeBatch;
    private StockBatch expiredBatch;
    private IssueNote issueNote;

    @BeforeEach
    void setUp() {
        maker = new User();
        maker.setId(1L);
        maker.setUsername("warehouse_staff");
        maker.setFullName("Nguyen Van A");

        checker = new User();
        checker.setId(2L);
        checker.setUsername("warehouse_manager");
        checker.setFullName("Tran Thi B");

        location = new WarehouseLocation();
        location.setId(1L);
        location.setLocationCode("RAW-A01-01");
        location.setZone(com.wms.util.ZoneType.RAW);

        product = new Product();
        product.setId(10L);
        product.setSkuCode("YMH-OIL-4T");
        product.setName("Dầu nhớt Yamalube 4T 1L");
        product.setManageByLot(true);
        product.setActive(true);

        activeBatch = new StockBatch();
        activeBatch.setId(101L);
        activeBatch.setProduct(product);
        activeBatch.setLocation(location);
        activeBatch.setLotNumber("LOT-202611-01");
        activeBatch.setExpDate(LocalDate.now().plusDays(30)); // Còn hạn 30 ngày
        activeBatch.setOnHandQty(BigDecimal.valueOf(100));
        activeBatch.setReservedQty(BigDecimal.valueOf(10));
        activeBatch.setStatus("AVAILABLE");

        expiredBatch = new StockBatch();
        expiredBatch.setId(102L);
        expiredBatch.setProduct(product);
        expiredBatch.setLocation(location);
        expiredBatch.setLotNumber("LOT-202610-99");
        expiredBatch.setExpDate(LocalDate.now().minusDays(1)); // Đã hết hạn hôm qua
        expiredBatch.setOnHandQty(BigDecimal.valueOf(50));
        expiredBatch.setReservedQty(BigDecimal.valueOf(10));
        expiredBatch.setStatus("AVAILABLE");

        issueNote = new IssueNote();
        issueNote.setId(1L);
        issueNote.setIssueNumber("IN-202611-001");
        issueNote.setMaker(maker);
        issueNote.setStatus(IssueStatus.PENDING_APPROVAL);
    }

    @Test
    @DisplayName("TC-FR03.03-N3: Chặn phê duyệt lệnh xuất khi có lô hàng bị hết hạn (BR-OUT-03, BR-OUT-04)")
    void tc_fr03_03_n3_approveBlockedWhenLotExpired() {
        // Arrange
        IssueNoteItem item = new IssueNoteItem();
        item.setId(1L);
        item.setIssueNote(issueNote);
        item.setStockBatch(expiredBatch);
        item.setRequestedQty(BigDecimal.TEN);
        item.setAllocatedQty(BigDecimal.TEN);

        List<IssueNoteItem> items = new ArrayList<>();
        items.add(item);
        issueNote.setItems(items);

        when(issueNoteRepository.findById(1L)).thenReturn(Optional.of(issueNote));
        when(ops.actor(eq(2L), eq(UserRole.ROLE_MANAGER))).thenReturn(checker);

        // Act & Assert
        ExpiredLotException exception = assertThrows(ExpiredLotException.class, () -> {
            issueService.approveIssueNote(1L, checker.getId());
        });

        assertTrue(exception.getMessage().contains("quá hạn"));
        verify(issueNoteRepository, never()).save(any(IssueNote.class));
    }

    @Test
    @DisplayName("TC-FR03.03-P: Phê duyệt lệnh xuất kho thành công khi tất cả lô còn hạn (BR-OUT-02)")
    void tc_fr03_03_p_approveSuccessWhenBatchesValid() {
        // Arrange
        IssueNoteItem item = new IssueNoteItem();
        item.setId(1L);
        item.setIssueNote(issueNote);
        item.setStockBatch(activeBatch);
        item.setRequestedQty(BigDecimal.TEN);
        item.setAllocatedQty(BigDecimal.TEN);

        List<IssueNoteItem> items = new ArrayList<>();
        items.add(item);
        issueNote.setItems(items);

        when(issueNoteRepository.findById(1L)).thenReturn(Optional.of(issueNote));
        when(ops.actor(eq(2L), eq(UserRole.ROLE_MANAGER))).thenReturn(checker);

        // Act
        IssueNoteResponseDTO response = issueService.approveIssueNote(1L, checker.getId());

        // Assert
        assertNotNull(response);
        assertEquals(IssueStatus.APPROVED_READY_TO_PICK, response.getStatus());
        assertEquals("Tran Thi B", response.getCheckerName());
    }

    @Test
    @DisplayName("TC-FR03.04-N: Chặn bàn giao hàng xuất kho khi lô hàng bị hết hạn tại thời điểm giao (BR-OUT-03)")
    void tc_fr03_04_n_confirmDispatchBlockedWhenLotExpired() {
        // Arrange
        issueNote.setStatus(IssueStatus.APPROVED_READY_TO_PICK);
        issueNote.setChecker(checker);

        IssueNoteItem item = new IssueNoteItem();
        item.setId(1L);
        item.setIssueNote(issueNote);
        item.setStockBatch(expiredBatch);
        item.setRequestedQty(BigDecimal.TEN);
        item.setAllocatedQty(BigDecimal.TEN);

        List<IssueNoteItem> items = new ArrayList<>();
        items.add(item);
        issueNote.setItems(items);

        when(issueNoteRepository.findById(1L)).thenReturn(Optional.of(issueNote));
        when(ops.actor(eq(1L), eq(UserRole.ROLE_STAFF))).thenReturn(maker);

        // Act & Assert
        ExpiredLotException exception = assertThrows(ExpiredLotException.class, () -> {
            issueService.confirmDispatch(1L, maker.getId());
        });

        assertTrue(exception.getMessage().contains("hết hạn"));
    }

    @Test
    @DisplayName("TC-FR03.01-N2: Tự động giải phóng lượng giữ chỗ khi lệnh xuất bị hủy bỏ (BR-OUT-01)")
    void tc_fr03_01_n2_cancelIssueReleasesReservedQuantity() {
        // Arrange
        IssueNoteItem item = new IssueNoteItem();
        item.setId(1L);
        item.setIssueNote(issueNote);
        item.setStockBatch(activeBatch);
        item.setRequestedQty(BigDecimal.TEN);
        item.setAllocatedQty(BigDecimal.TEN);

        List<IssueNoteItem> items = new ArrayList<>();
        items.add(item);
        issueNote.setItems(items);

        assertEquals(BigDecimal.valueOf(10), activeBatch.getReservedQty());

        when(issueNoteRepository.findById(1L)).thenReturn(Optional.of(issueNote));
        when(ops.actor(eq(1L), eq(UserRole.ROLE_STAFF))).thenReturn(maker);

        // Act
        issueService.cancelIssueNote(1L, "Khách hủy đơn hàng", maker.getId());

        // Assert: reserved_qty được giải phóng về 0
        assertEquals(BigDecimal.ZERO, activeBatch.getReservedQty());
        assertEquals(IssueStatus.CANCELLED, issueNote.getStatus());
    }
}
