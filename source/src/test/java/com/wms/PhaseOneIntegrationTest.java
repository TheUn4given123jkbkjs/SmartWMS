package com.wms;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wms.dto.auth.UserResponseDTO;
import com.wms.dto.purchase.PurchaseOrderImportDTO;
import com.wms.entity.Product;
import com.wms.entity.StockBatch;
import com.wms.entity.WarehouseLocation;
import com.wms.exception.SecurityViolationException;
import com.wms.repository.CategoryRepository;
import com.wms.repository.IssueNoteRepository;
import com.wms.repository.ProductRepository;
import com.wms.repository.PurchaseOrderRepository;
import com.wms.repository.ReceiptNoteRepository;
import com.wms.repository.StockBatchRepository;
import com.wms.repository.WarehouseLocationRepository;
import com.wms.service.PurchaseOrderService;
import com.wms.service.PurchaseOrderService.ImportLine;
import com.wms.service.PurchaseOrderService.ImportRequest;
import com.wms.util.IssueStatus;
import com.wms.util.LocationStatus;
import com.wms.util.ReceiptStatus;
import com.wms.util.ZoneType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class PhaseOneIntegrationTest {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper json;
  @Autowired CategoryRepository categories;
  @Autowired ProductRepository products;
  @Autowired WarehouseLocationRepository locations;
  @Autowired StockBatchRepository batches;
  @Autowired PurchaseOrderRepository orders;
  @Autowired ReceiptNoteRepository receipts;
  @Autowired IssueNoteRepository issues;
  @Autowired PurchaseOrderService purchaseOrderService;

  private MockHttpSession staff;
  private MockHttpSession manager;
  private MockHttpSession admin;
  private Product product;
  private String poNumber;
  private String tag;

  @BeforeEach
  void setUp() throws Exception {
    staff = login("staff_thien");
    manager = login("manager_trung");
    admin = login("admin");
    tag = UUID.randomUUID().toString().substring(0, 8);
    poNumber = "PO-PHASE1-" + tag;

    Product item = new Product();
    item.setCategory(categories.findAll().get(0));
    item.setSkuCode("PHASE1-" + tag);
    item.setName("Phase 1 " + tag);
    item.setUom("Cái");
    item.setMaxStock(new BigDecimal("100"));
    product = products.save(item);
  }

  @ParameterizedTest
  @CsvSource({"/api/v1/purchase-orders,201", "/receipt/api/purchase-orders,200"})
  void onlyManagerCanImportApprovedPo(String path, int success) throws Exception {
    postAs(path, poPayload(), null).andExpect(status().isUnauthorized());
    postAs(path, poPayload(), staff).andExpect(status().isForbidden());
    postAs(path, poPayload(), admin).andExpect(status().isForbidden());
    assertTrue(orders.findByPoNumber(poNumber).isEmpty());

    postAs(path, poPayload(), manager)
      .andExpect(status().is(success))
      .andExpect(jsonPath("$.poNumber").value(poNumber))
      .andExpect(jsonPath("$.status").value("APPROVED"));
    assertTrue(orders.findByPoNumber(poNumber).isPresent());
    postAs(path, poPayload(), manager).andExpect(status().isConflict());
  }

  @Test
  void bothPoServiceMethodsCheckTheActorsRole() {
    var legacy = new PurchaseOrderImportDTO(
      poNumber,
      "Phase 1 Supplier",
      List.of(new PurchaseOrderImportDTO.Item(product.getId(), BigDecimal.TEN))
    );
    var workspace = new ImportRequest(
      poNumber,
      "Phase 1 Supplier",
      List.of(new ImportLine(product.getId(), BigDecimal.TEN))
    );
    for (Long actorId : new Long[] { null, userId(staff), userId(admin) }) {
      assertThrows(SecurityViolationException.class, () ->
        purchaseOrderService.importPurchaseOrder(legacy, actorId)
      );
      assertThrows(SecurityViolationException.class, () ->
        purchaseOrderService.importApproved(workspace, actorId)
      );
    }
    assertTrue(orders.findByPoNumber(poNumber).isEmpty());
  }

  @Test
  void maintenanceBlocksReceiptCreationAndApproval() throws Exception {
    long locationId = createLocation(ZoneType.RAW);
    importPo();
    long receiptId = responseId(createReceipt(locationId, staff), 201);
    markMaintenance(locationId);

    createReceipt(locationId, staff)
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("Không được cất hàng vào ô đang bảo trì."));
    approveReceipt(receiptId, manager)
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("Không được cất hàng vào ô đang bảo trì."));
    assertEquals(ReceiptStatus.PENDING_APPROVAL, receipts.findById(receiptId).orElseThrow().getStatus());
    assertTrue(batches.findByProductId(product.getId()).isEmpty());
    assertEquals(LocationStatus.MAINTENANCE, locations.findById(locationId).orElseThrow().getStatus());
  }

  @ParameterizedTest
  @EnumSource(value = ZoneType.class, names = {"DOCK", "INSP", "SCRAP"})
  void nonStorageZonesRejectReceipts(ZoneType zone) throws Exception {
    long locationId = createLocation(zone);
    importPo();
    createReceipt(locationId, staff)
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("Chỉ được cất hàng ở RAW hoặc FG."));
    assertTrue(batches.findByProductId(product.getId()).isEmpty());
  }

  @ParameterizedTest
  @EnumSource(value = ZoneType.class, names = {"RAW", "FG"})
  void storageZonesAcceptReceiptsAndUpdateLocationStatus(ZoneType zone) throws Exception {
    long locationId = receiveStock(zone);
    assertEquals(LocationStatus.OCCUPIED, locations.findById(locationId).orElseThrow().getStatus());
    assertQuantity(10, batch().getOnHandQty());
  }

  @Test
  void dispatchOfEligibleBatchKeepsLocationInMaintenance() throws Exception {
    long locationId = receiveStock(ZoneType.RAW);
    long issueId = responseId(createIssue(10, null, staff), 201);
    postIssueAction(issueId, "approve", Map.of(), manager).andExpect(status().isOk());
    markMaintenance(locationId);

    postIssueAction(issueId, "dispatch", Map.of(), staff)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("COMPLETED"));
    assertQuantity(0, batch().getOnHandQty());
    assertQuantity(0, batch().getReservedQty());
    assertEquals(LocationStatus.MAINTENANCE, locations.findById(locationId).orElseThrow().getStatus());
  }

  @Test
  void adminCannotWriteReceiptOrIssueNotes() throws Exception {
    long locationId = receiveStock(ZoneType.RAW);
    createReceipt(locationId, admin).andExpect(status().isForbidden());
    long receiptId = responseId(createReceipt(locationId, staff), 201);
    approveReceipt(receiptId, admin).andExpect(status().isForbidden());

    createIssue(3, null, admin).andExpect(status().isForbidden());
    long issueId = responseId(createIssue(3, null, staff), 201);
    postIssueAction(issueId, "approve", Map.of(), admin).andExpect(status().isForbidden());
    postIssueAction(issueId, "approve", Map.of(), manager).andExpect(status().isOk());
    postIssueAction(issueId, "dispatch", Map.of(), admin).andExpect(status().isForbidden());
    assertQuantity(10, batch().getOnHandQty());
    assertQuantity(3, batch().getReservedQty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"EXPIRED", "QUARANTINED", "BLOCKED"})
  void ineligibleLotsBlockNormalIssueAndKeepReservationsUntilRelease(String state) throws Exception {
    receiveStock(ZoneType.RAW);
    long approved = responseId(createIssue(3, null, staff), 201);
    long pending = responseId(createIssue(2, null, staff), 201);
    postIssueAction(approved, "approve", Map.of(), manager).andExpect(status().isOk());

    StockBatch batch = batch();
    if ("EXPIRED".equals(state)) {
      batch.setExpDate(LocalDate.now().minusDays(1));
    } else {
      batch.setStatus(state);
    }
    long batchId = batches.save(batch).getId();

    mvc.perform(get("/inventory/api/batches").session(staff).param("keyword", product.getSkuCode()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].status").value(state))
      .andExpect(jsonPath("$[0].availableQty").value(0));
    mvc.perform(get("/issue/api/propose-fefo").session(staff)
        .param("productId", product.getId().toString()).param("requestedQty", "1"))
      .andExpect(status().isBadRequest());
    createIssue(1, batchId, staff).andExpect(status().isConflict());
    postIssueAction(pending, "approve", Map.of(), manager).andExpect(status().isConflict());
    postIssueAction(approved, "dispatch", Map.of(), staff).andExpect(status().isConflict());
    assertEquals(IssueStatus.PENDING_APPROVAL, issues.findById(pending).orElseThrow().getStatus());
    assertEquals(IssueStatus.APPROVED_READY_TO_PICK, issues.findById(approved).orElseThrow().getStatus());
    assertQuantity(10, batch().getOnHandQty());
    assertQuantity(5, batch().getReservedQty());

    var decision = Map.of("reason", "Lô không còn đủ điều kiện xuất.");
    postIssueAction(pending, "reject", decision, manager).andExpect(status().isOk());
    assertQuantity(3, batch().getReservedQty());
    postIssueAction(approved, "cancel", decision, manager).andExpect(status().isNoContent());
    assertQuantity(0, batch().getReservedQty());
    assertQuantity(10, batch().getOnHandQty());
    assertQuantity(0, batch().getAvailableQty());
  }

  private MockHttpSession login(String username) throws Exception {
    return (MockHttpSession) postAs(
      "/auth/api/login", Map.of("username", username, "password", "123456"), null
    ).andExpect(status().isOk()).andReturn().getRequest().getSession(false);
  }

  private Long userId(MockHttpSession session) {
    return ((UserResponseDTO) session.getAttribute("user")).getId();
  }

  private ResultActions postAs(String path, Object body, MockHttpSession session) throws Exception {
    var request = post(path).contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(body));
    if (session != null) request.session(session);
    return mvc.perform(request);
  }

  private long responseId(ResultActions result, int statusCode) throws Exception {
    return json.readTree(
      result.andExpect(status().is(statusCode)).andReturn().getResponse().getContentAsString()
    ).path("id").asLong();
  }

  private Map<String, Object> poPayload() {
    return Map.of(
      "poNumber", poNumber,
      "supplierName", "Phase 1 Supplier",
      "items", List.of(Map.of("productId", product.getId(), "orderedQty", 100))
    );
  }

  private void importPo() throws Exception {
    postAs("/receipt/api/purchase-orders", poPayload(), manager).andExpect(status().isOk());
  }

  private long createLocation(ZoneType zone) {
    WarehouseLocation location = new WarehouseLocation();
    location.setLocationCode(zone.name() + "-" + tag);
    location.setZone(zone);
    location.setMaxCapacity(new BigDecimal("1000"));
    return locations.save(location).getId();
  }

  private void markMaintenance(long locationId) {
    WarehouseLocation location = locations.findById(locationId).orElseThrow();
    location.setStatus(LocationStatus.MAINTENANCE);
    locations.save(location);
  }

  private long receiveStock(ZoneType zone) throws Exception {
    long locationId = createLocation(zone);
    importPo();
    long receiptId = responseId(createReceipt(locationId, staff), 201);
    approveReceipt(receiptId, manager).andExpect(status().isOk());
    return locationId;
  }

  private ResultActions createReceipt(long locationId, MockHttpSession session) throws Exception {
    return postAs("/receipt/api/notes", Map.of(
      "poNumber", poNumber,
      "supplierName", "Phase 1 Supplier",
      "items", List.of(Map.of(
        "productId", product.getId(),
        "locationId", locationId,
        "batchCode", "LOT-" + tag,
        "expiryDate", LocalDate.now().plusDays(45).toString(),
        "actualQty", 10
      ))
    ), session);
  }

  private ResultActions approveReceipt(long receiptId, MockHttpSession session) throws Exception {
    return postAs("/receipt/api/notes/" + receiptId + "/approve", Map.of("approved", true), session);
  }

  private ResultActions createIssue(int qty, Long selectedBatchId, MockHttpSession session) throws Exception {
    var line = new HashMap<String, Object>(Map.of("productId", product.getId(), "requestedQty", qty));
    if (selectedBatchId != null) line.put("selectedBatchId", selectedBatchId);
    return postAs("/issue/api/notes", Map.of(
      "orderNumber", "ORDER-" + tag,
      "customerName", "Sản xuất",
      "strategy", "FEFO",
      "exportType", "PRODUCTION",
      "items", List.of(line)
    ), session);
  }

  private ResultActions postIssueAction(
    long issueId, String action, Object body, MockHttpSession session
  ) throws Exception {
    return postAs("/issue/api/notes/" + issueId + "/" + action, body, session);
  }

  private StockBatch batch() {
    return batches.findByProductId(product.getId()).get(0);
  }

  private void assertQuantity(int expected, BigDecimal actual) {
    assertEquals(0, BigDecimal.valueOf(expected).compareTo(actual));
  }
}
