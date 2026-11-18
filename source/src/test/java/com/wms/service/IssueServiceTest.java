package com.wms.service;

import com.wms.dto.issue.IssueNoteResponseDTO;
import com.wms.entity.*;
import com.wms.exception.ExpiredLotException;
import com.wms.exception.SecurityViolationException;
import com.wms.repository.*;
import com.wms.service.impl.IssueServiceImpl;
import com.wms.util.IssueStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private IssueNoteRepository issueNoteRepository;

    @Mock
    private IssueNoteItemRepository issueNoteItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockBatchRepository stockBatchRepository;

    @Mock
    private StockCardRepository stockCardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private IssueServiceImpl issueService;

    private User maker;
    private User checker;
    private Product product;
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

        product = new Product();
        product.setId(10L);
        product.setSkuCode("YMH-OIL-4T");
        product.setName("Dầu nhớt Yamalube 4T 1L");

        activeBatch = new StockBatch();
        activeBatch.setId(101L);
        activeBatch.setProduct(product);
        activeBatch.setLotNumber("LOT-202611-01");
        activeBatch.setExpDate(LocalDate.now().plusDays(30)); // Còn hạn 30 ngày
        activeBatch.setOnHandQty(100);
        activeBatch.setReservedQty(10);
        activeBatch.setStatus("AVAILABLE");

        expiredBatch = new StockBatch();
        expiredBatch.setId(102L);
        expiredBatch.setProduct(product);
        expiredBatch.setLotNumber("LOT-202610-99");
        expiredBatch.setExpDate(LocalDate.now().minusDays(1)); // Đã hết hạn hôm qua
        expiredBatch.setOnHandQty(50);
        expiredBatch.setReservedQty(10);
        expiredBatch.setStatus("AVAILABLE");

        issueNote = new IssueNote();
        issueNote.setId(1L);
        issueNote.setIssueNumber("IN-202611-001");
        issueNote.setMaker(maker);
        issueNote.setStatus(IssueStatus.PENDING_APPROVAL);
    }

    @Test
    @DisplayName("D4 Test 1: Quản lý duyệt phiếu xuất có lô bị hết hạn -> Chặn duyệt, ném ExpiredLotException")
    void testApproveIssueNote_WhenLotExpired_ThrowsExpiredLotException() {
        // Arrange: Phiếu xuất có 1 lô hàng đã bị hết hạn trong thời gian chờ duyệt
        IssueNoteItem item = new IssueNoteItem();
        item.setId(1L);
        item.setIssueNote(issueNote);
        item.setStockBatch(expiredBatch);
        item.setRequestedQty(10);
        item.setAllocatedQty(10);

        List<IssueNoteItem> items = new ArrayList<>();
        items.add(item);
        issueNote.setItems(items);

        when(issueNoteRepository.findById(1L)).thenReturn(Optional.of(issueNote));

        // Act & Assert
        ExpiredLotException exception = assertThrows(ExpiredLotException.class, () -> {
            issueService.approveIssueNote(1L, checker.getId());
        });

        assertTrue(exception.getMessage().contains("đã quá hạn sử dụng"));
        verify(issueNoteRepository, never()).save(any(IssueNote.class));
    }

    @Test
    @DisplayName("D4 Test 2: Quản lý duyệt phiếu xuất khi tất cả lô còn hạn -> Duyệt thành công")
    void testApproveIssueNote_WhenAllBatchesValid_Success() {
        // Arrange: Phiếu xuất chỉ có lô còn hạn
        IssueNoteItem item = new IssueNoteItem();
        item.setId(1L);
        item.setIssueNote(issueNote);
        item.setStockBatch(activeBatch);
        item.setRequestedQty(10);
        item.setAllocatedQty(10);

        List<IssueNoteItem> items = new ArrayList<>();
        items.add(item);
        issueNote.setItems(items);

        when(issueNoteRepository.findById(1L)).thenReturn(Optional.of(issueNote));
        when(userRepository.findById(checker.getId())).thenReturn(Optional.of(checker));
        when(issueNoteRepository.save(any(IssueNote.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        IssueNoteResponseDTO response = issueService.approveIssueNote(1L, checker.getId());

        // Assert
        assertNotNull(response);
        assertEquals(IssueStatus.APPROVED_READY_TO_PICK, response.getStatus());
        assertEquals("Tran Thi B", response.getCheckerName());
    }

    @Test
    @DisplayName("D4 Test 3: Bàn giao hàng xuất kho có lô bị hết hạn -> Chặn giao, ném ExpiredLotException")
    void testConfirmDispatch_WhenLotExpired_ThrowsExpiredLotException() {
        // Arrange: Phiếu xuất đã duyệt nhưng khi bốc hàng lô bị quá hạn
        issueNote.setStatus(IssueStatus.APPROVED_READY_TO_PICK);
        issueNote.setChecker(checker);

        IssueNoteItem item = new IssueNoteItem();
        item.setId(1L);
        item.setIssueNote(issueNote);
        item.setStockBatch(expiredBatch);
        item.setRequestedQty(10);
        item.setAllocatedQty(10);

        List<IssueNoteItem> items = new ArrayList<>();
        items.add(item);
        issueNote.setItems(items);

        when(issueNoteRepository.findById(1L)).thenReturn(Optional.of(issueNote));

        // Act & Assert
        ExpiredLotException exception = assertThrows(ExpiredLotException.class, () -> {
            issueService.confirmDispatch(1L, maker.getId());
        });

        assertTrue(exception.getMessage().contains("đã quá hạn sử dụng tại thời điểm bốc hàng"));
        verify(stockBatchRepository, never()).save(any(StockBatch.class));
    }

    @Test
    @DisplayName("D4 Test 4: Hủy phiếu xuất kho -> Giải phóng số lượng tạm giữ chỗ (reserved_qty)")
    void testCancelIssueNote_ReleasesReservedQuantity() {
        // Arrange
        IssueNoteItem item = new IssueNoteItem();
        item.setId(1L);
        item.setIssueNote(issueNote);
        item.setStockBatch(activeBatch);
        item.setRequestedQty(10);
        item.setAllocatedQty(10);

        List<IssueNoteItem> items = new ArrayList<>();
        items.add(item);
        issueNote.setItems(items);

        assertEquals(10, activeBatch.getReservedQty());

        when(issueNoteRepository.findById(1L)).thenReturn(Optional.of(issueNote));

        // Act
        issueService.cancelIssueNote(1L, "Khách hủy đơn hàng", maker.getId());

        // Assert: reserved_qty được giải phóng về 0
        assertEquals(0, activeBatch.getReservedQty());
        assertEquals(IssueStatus.CANCELLED, issueNote.getStatus());
        verify(stockBatchRepository).save(activeBatch);
        verify(issueNoteRepository).save(issueNote);
    }
}
