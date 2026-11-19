package com.wms;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.*;
import com.wms.dto.auth.UserResponseDTO;
import com.wms.entity.*;
import com.wms.repository.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MustHaveIntegrationTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper json;

  @Autowired
  StockBatchRepository batches;

  @Autowired
  StockCardRepository cards;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  IssueNoteRepository issues;

  @Autowired
  IssueNoteItemRepository allocations;

  @Autowired
  ReceiptNoteRepository receipts;

  @Autowired
  PurchaseOrderRepository orders;

  @Autowired
  JdbcTemplate jdbc;

  MockHttpSession staff, manager, admin;
  long product, location;
  String po;

  JsonNode request(
    String method,
    String url,
    Object body,
    MockHttpSession session,
    int expected
  ) throws Exception {
    var builder =
      switch (method) {
        case "POST" -> post(url);
        case "PUT" -> put(url);
        default -> get(url);
      };
    var response = mvc
      .perform(
        builder
          .session(session)
          .contentType("application/json")
          .content(json.writeValueAsString(body == null ? Map.of() : body))
      )
      .andExpect(status().is(expected))
      .andReturn()
      .getResponse();
    return response.getContentAsString().isBlank()
      ? json.nullNode()
      : json.readTree(response.getContentAsString());
  }

  MockHttpSession login(String name) throws Exception {
    return (MockHttpSession) mvc
      .perform(
        post("/auth/api/login")
          .contentType("application/json")
          .content(
            json.writeValueAsString(
              Map.of("username", name, "password", "123456")
            )
          )
      )
      .andExpect(status().isOk())
      .andReturn()
      .getRequest()
      .getSession();
  }

  @BeforeEach
  void fixture() throws Exception {
    staff = login("staff_thien");
    manager = login("manager_trung");
    admin = login("manager_khang");
    String tag = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    long category = request("GET", "/catalog/api/categories", null, admin, 200)
      .get(0)
      .get("id")
      .asLong();
    product = request(
      "POST",
      "/catalog/api/products",
      Map.of(
        "skuCode",
        "CORE-" + tag,
        "name",
        "Core " + tag,
        "categoryId",
        category,
        "uom",
        "Kg",
        "minStock",
        new BigDecimal("5.25"),
        "maxStock",
        new BigDecimal("100.50"),
        "manageByLot",
        true
      ),
      admin,
      201
    )
      .get("id")
      .asLong();
    request(
      "POST",
      "/catalog/api/locations",
      Map.of(
        "locationCode",
        "RAW-" + tag,
        "zone",
        "RAW",
        "maxCapacity",
        10000
      ),
      admin,
      201
    );
    for (JsonNode l : request(
      "GET",
      "/catalog/api/locations",
      null,
      admin,
      200
    )) if (l.get("locationCode").asText().equals("RAW-" + tag)) location = l
      .get("id")
      .asLong();
    po = "PO-" + tag;
    request(
      "POST",
      "/receipt/api/purchase-orders",
      Map.of(
        "poNumber",
        po,
        "supplierName",
        "Supplier",
        "items",
        List.of(Map.of("productId", product, "orderedQty", 100))
      ),
      manager,
      200
    );
  }

  Map<String, Object> receiptLine(String lot, String qty, int expiry) {
    return new HashMap<>(
      Map.of(
        "productId",
        product,
        "locationId",
        location,
        "batchCode",
        lot,
        "expiryDate",
        LocalDate.now().plusDays(expiry).toString(),
        "actualQty",
        new BigDecimal(qty),
        "plannedQty",
        100
      )
    );
  }

  JsonNode receipt(String lot, String qty, int exp, int expected)
    throws Exception {
    return request(
      "POST",
      "/receipt/api/notes",
      Map.of(
        "poNumber",
        po,
        "supplierName",
        "Supplier",
        "items",
        List.of(receiptLine(lot, qty, exp))
      ),
      staff,
      expected
    );
  }

  void approveReceipt(long id) throws Exception {
    request(
      "POST",
      "/receipt/api/notes/" + id + "/approve",
      Map.of("approved", true),
      manager,
      200
    );
  }

  JsonNode issue(String qty, int expected) throws Exception {
    return request(
      "POST",
      "/issue/api/notes",
      Map.of(
        "orderNumber",
        "ORDER",
        "customerName",
        "Production",
        "strategy",
        "FEFO",
        "exportType",
        "PRODUCTION",
        "items",
        List.of(
          Map.of("productId", product, "requestedQty", new BigDecimal(qty))
        )
      ),
      staff,
      expected
    );
  }

  JsonNode stock() throws Exception {
    for (JsonNode row : request(
      "GET",
      "/inventory/api/available-stock",
      null,
      staff,
      200
    )) if (row.get("productId").asLong() == product) return row;
    throw new AssertionError();
  }

  void number(String field, String expected) throws Exception {
    assertEquals(
      0,
      stock().get(field).decimalValue().compareTo(new BigDecimal(expected)),
      field
    );
  }

  @Test
  void rejectedReceiptReleasesPoAndRecordsMakerAndChecker() throws Exception {
    JsonNode created = receipt("REJECT-RECEIPT", "100", 31, 201);
    long id = created.path("id").asLong();
    String path = "/receipt/api/notes/" + id + "/approve";
    request("POST", path, Map.of("approved", false), manager, 400);
    request("POST", path, Map.of("approved", false, "reviewNote", "Sai quy cách"), staff, 403);
    JsonNode rejected = request("POST", path,
      Map.of("approved", false, "reviewNote", "Sai quy cách"), manager, 200);
    assertEquals("REJECTED", rejected.path("status").asText());
    number("totalOnHandQty", "0");
    assertTrue(cards.findByProductIdOrderByTransactionDateDesc(product).isEmpty());
    assertAudit(created.path("receiptCode").asText(),
      List.of("CREATE_RECEIPT", "REJECT_RECEIPT"), List.of(staff, manager));
    request("POST", path, Map.of("approved", true), manager, 409);
    approveReceipt(receipt("REPLACEMENT", "100", 31, 201).path("id").asLong());
    number("totalOnHandQty", "100");
  }

  @Test
  void approvedReceiptAndIssuePersistAuditEventsWithActors() throws Exception {
    JsonNode receipt = receipt("AUDITED", "10", 31, 201);
    approveReceipt(receipt.path("id").asLong());
    assertAudit(receipt.path("receiptCode").asText(),
      List.of("CREATE_RECEIPT", "COMPLETE_RECEIPT"), List.of(staff, manager));
    JsonNode issue = issue("3", 201);
    String path = "/issue/api/notes/" + issue.path("id").asLong();
    request("POST", path + "/approve", null, manager, 200);
    request("POST", path + "/dispatch", null, staff, 200);
    assertAudit(issue.path("issueCode").asText(),
      List.of("RESERVE_ISSUE", "APPROVE_ISSUE", "COMPLETE_ISSUE"), List.of(staff, manager, staff));
    number("totalOnHandQty", "7");
    number("totalReservedQty", "0");
  }

  private void assertAudit(String reference, List<String> actions, List<MockHttpSession> actors) {
    assertEquals(actions, jdbc.queryForList(
      "SELECT action FROM audit_event WHERE reference_code = ? AND occurred_at IS NOT NULL ORDER BY id",
      String.class, reference));
    assertEquals(actors.stream().map(session -> ((UserResponseDTO) session.getAttribute("user")).getId()).toList(),
      jdbc.queryForList("SELECT \"actor_id\" FROM audit_event WHERE reference_code = ? ORDER BY id",
        Long.class, reference));
  }

  @Test
  void allRolesCanReadInventoryAndManagerCanWriteCatalog() throws Exception {
    for (var session : List.of(staff, manager, admin)) {
      request("GET", "/inventory/api/available-stock", null, session, 200);
      request("GET", "/inventory/api/alerts/low-stock", null, session, 200);
      request("GET", "/inventory/api/stock-cards?productId=" + product, null, session, 200);
    }
    request("POST", "/catalog/api/locations", Map.of(
      "locationCode", "FG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
      "zone", "FG", "maxCapacity", 100), manager, 201);
  }

  @Test
  void lowStockUsesAvailableAndOffsetsApprovedPoBalance() throws Exception {
    request("PUT", "/catalog/api/products/" + product + "/limits",
      Map.of("minStock", 10, "maxStock", 30), manager, 200);
    approveReceipt(receipt("LOW-STOCK", "100", 40, 201).path("id").asLong());
    issue("89", 201);
    number("availableQty", "11");
    number("suggestedOrderQty", "0");
    assertFalse(stock().path("underMin").asBoolean());
    assertTrue(lowStockAlert().isEmpty());

    issue("1", 201);
    assertLowStock("10", "0", "20");
    issue("2", 201);
    assertLowStock("8", "0", "22");

    po += "-INCOMING";
    request("POST", "/receipt/api/purchase-orders",
      Map.of("poNumber", po, "supplierName", "Supplier",
        "items", List.of(Map.of("productId", product, "orderedQty", 7))),
      manager, 200);
    approveReceipt(receipt("PO-PARTIAL", "2", 40, 201).path("id").asLong());
    issue("2", 201);
    assertLowStock("8", "5", "0");
    receipt("PO-PENDING", "1", 40, 201);
    assertLowStock("8", "5", "0");
  }

  private Optional<JsonNode> lowStockAlert() throws Exception {
    for (JsonNode row : request("GET", "/inventory/api/alerts/low-stock", null, staff, 200)) {
      if (row.path("productId").asLong() == product) return Optional.of(row);
    }
    return Optional.empty();
  }

  private void assertLowStock(String available, String onOrder, String suggestion) throws Exception {
    JsonNode row = lowStockAlert().orElseThrow();
    assertTrue(row.path("underMin").asBoolean());
    assertEquals(0, new BigDecimal(available).compareTo(row.path("availableQty").decimalValue()));
    assertEquals(0, new BigDecimal(onOrder).compareTo(row.path("onOrder").decimalValue()));
    assertEquals(0, new BigDecimal(suggestion).compareTo(row.path("suggestedOrderQty").decimalValue()));
  }

  @ParameterizedTest
  @ValueSource(ints = {31, 30, 1, 0, -1})
  void receiptUsesCurrentExpiryThreshold(int remaining) throws Exception {
    JsonNode result = receipt("INBOUND-EXPIRY", "1", remaining, remaining > 0 ? 201 : 400);
    if (remaining > 0) {
      approveReceipt(result.path("id").asLong());
      number("totalOnHandQty", "1");
    } else {
      assertFalse(result.path("validationErrors").path("items[0].expiryDate").isMissingNode());
      number("totalOnHandQty", "0");
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1})
  void receiptApprovalRechecksExpiryAfterWaiting(int remaining) throws Exception {
    long id = receipt("WAITING-EXPIRY", "1", 1, 201).path("id").asLong();
    assertEquals(1, jdbc.update("UPDATE receipt_note_item SET exp_date = ? WHERE receipt_id = ?",
      LocalDate.now().plusDays(remaining), id));
    request("POST", "/receipt/api/notes/" + id + "/approve",
      Map.of("approved", true), manager, 409);
    assertEquals("PENDING_APPROVAL", receipts.findById(id).orElseThrow().getStatus().name());
    number("totalOnHandQty", "0");
  }

  @Test
  void nearExpiryUsesInclusiveThresholdAndD3ExpiryBoundary() throws Exception {
    for (int days : List.of(1, 7, 8, 30, 31)) {
      approveReceipt(receipt("EXP-" + days, "1", days, 201).path("id").asLong());
    }
    for (String lot : List.of("TODAY", "PAST", "NO-DATE", "EMPTY-LOT")) {
      approveReceipt(receipt(lot, "1", 10, 201).path("id").asLong());
    }
    LocalDate today = LocalDate.now();
    jdbc.update("UPDATE stock_batch SET exp_date = ? WHERE product_id = ? AND lot_number = 'TODAY'",
      today, product);
    jdbc.update("UPDATE stock_batch SET exp_date = ? WHERE product_id = ? AND lot_number = 'PAST'",
      today.minusDays(1), product);
    jdbc.update("UPDATE stock_batch SET exp_date = NULL WHERE product_id = ? AND lot_number = 'NO-DATE'",
      product);
    jdbc.update("UPDATE stock_batch SET on_hand_qty = 0 WHERE product_id = ? AND lot_number = 'EMPTY-LOT'",
      product);

    Map<String, String> defaultAlerts = expiryLevels("");
    assertEquals(Map.of("TODAY", "CRITICAL", "PAST", "EXPIRED", "EXP-1", "CRITICAL",
      "EXP-7", "CRITICAL", "EXP-8", "WARNING", "EXP-30", "WARNING"), defaultAlerts);
    assertEquals(defaultAlerts, expiryLevels("?thresholdDays=30"));
    var extended = new HashMap<>(defaultAlerts);
    extended.put("EXP-31", "WARNING");
    assertEquals(extended, expiryLevels("?thresholdDays=31"));
    assertEquals(extended, expiryLevels("?thresholdDays=365"));
    assertEquals(Map.of("TODAY", "CRITICAL", "PAST", "EXPIRED", "EXP-1", "CRITICAL"),
      expiryLevels("?thresholdDays=1"));
    request("GET", "/inventory/api/alerts/near-expiry?thresholdDays=366", null, staff, 400);
  }

  private Map<String, String> expiryLevels(String query) throws Exception {
    String sku = productRepository.findById(product).orElseThrow().getSkuCode();
    Map<String, String> result = new HashMap<>();
    for (JsonNode row : request("GET", "/inventory/api/alerts/near-expiry" + query, null, staff, 200)) {
      if (sku.equals(row.path("skuCode").asText())) {
        result.put(row.path("batchCode").asText(), row.path("alertLevel").asText());
      }
    }
    return result;
  }

  @Test
  void splitIssuePersistsDecimalAllocationsAndSkuBalances() throws Exception {
    approveReceipt(receipt("SPLIT-FIRST", "5.25", 15, 201).get("id").asLong());
    approveReceipt(receipt("SPLIT-SECOND", "7.75", 35, 201).get("id").asLong());
    JsonNode created = issue("7.50", 201);
    long id = created.get("id").asLong();
    assertFalse(created.path("isOverridden").asBoolean(true));
    assertEquals("ORDER", created.path("orderNumber").asText());
    assertEquals("Production", created.path("customerName").asText());
    assertEquals("PRODUCTION", created.path("exportType").asText());
    assertEquals(2, created.path("pickList").size());
    var lines = allocations.findByIssueNoteId(id).stream()
      .sorted(Comparator.comparing(IssueNoteItem::getId))
      .toList();
    assertEquals(2, lines.size());
    assertEquals(List.of(new BigDecimal("5.25"), new BigDecimal("2.25")),
      lines.stream().map(IssueNoteItem::getAllocatedQty).toList());
    assertEquals(lines.stream().map(IssueNoteItem::getAllocatedQty).toList(),
      lines.stream().map(IssueNoteItem::getRequestedQty).toList());

    request("POST", "/issue/api/notes/" + id + "/approve", null, manager, 200);
    request("POST", "/issue/api/notes/" + id + "/dispatch", null, staff, 200);
    var entries = cards.findByProductIdOrderByTransactionDateDesc(product).stream()
      .sorted(Comparator.comparing(StockCard::getId))
      .toList();
    assertEquals(List.of(new BigDecimal("5.25"), new BigDecimal("7.75"),
        new BigDecimal("-5.25"), new BigDecimal("-2.25")),
      entries.stream().map(StockCard::getQtyChanged).toList());
    assertEquals(List.of(new BigDecimal("5.25"), new BigDecimal("13.00"),
        new BigDecimal("7.75"), new BigDecimal("5.50")),
      entries.stream().map(StockCard::getAfterChanged).toList());
    number("totalOnHandQty", "5.50");
    number("totalReservedQty", "0");
  }

  @Test
  void overrideRequiresTenCharactersAndStaysVisibleAfterApproval() throws Exception {
    approveReceipt(receipt("OVERRIDE-CHECK", "10", 35, 201).get("id").asLong());
    var data = new HashMap<String, Object>(Map.of(
      "orderNumber", "OVERRIDE-ORDER",
      "customerName", "Production",
      "strategy", "FIFO",
      "items", List.of(Map.of("productId", product, "requestedQty", 2))
    ));
    for (String reason : List.of("  ", " 123456789 ", "a".repeat(256))) {
      data.put("overrideReason", reason);
      request("POST", "/issue/api/notes", data, staff, 400);
      number("totalReservedQty", "0");
    }
    data.put("overrideReason", "  1234567890  ");
    JsonNode created = request("POST", "/issue/api/notes", data, staff, 201);
    assertTrue(created.path("isOverridden").asBoolean());
    assertEquals("1234567890", created.path("overrideReason").asText());
    long id = created.path("id").asLong();
    request("POST", "/issue/api/notes/" + id + "/approve", null, manager, 200);
    JsonNode stored = request("GET", "/issue/api/notes/" + id, null, manager, 200);
    assertEquals("APPROVED_READY_TO_PICK", stored.path("status").asText());
    assertTrue(stored.path("isOverridden").asBoolean());
    assertEquals("1234567890", stored.path("overrideReason").asText());
  }

  @Test
  void quantityInputsEnforceDecimalPrecisionAndScale() throws Exception {
    for (String invalid : List.of("1.001", "10000000000.00")) {
      receipt("INVALID-DECIMAL", invalid, 35, 400);
      issue(invalid, 400);
    }
    String path = "/receipt/api/purchase-orders";
    String number = po + "-LIMIT";
    for (String invalid : List.of("1.001", "10000000000.00")) {
      request("POST", path, Map.of("poNumber", number, "supplierName", "Supplier",
        "items", List.of(Map.of("productId", product, "orderedQty", new BigDecimal(invalid)))),
        manager, 400);
      assertTrue(orders.findByPoNumber(number).isEmpty());
    }
    JsonNode imported = request("POST", path, Map.of("poNumber", number, "supplierName", "Supplier",
      "items", List.of(Map.of("productId", product, "orderedQty", new BigDecimal("9999999999.99")))),
      manager, 200);
    assertEquals(0, imported.path("items").get(0).path("orderedQty").decimalValue()
      .compareTo(new BigDecimal("9999999999.99")));
  }

  @Test
  void receiptReserveApproveHandoverAndLedgerBalance() throws Exception {
    long id = receipt("LOT-A", "20.50", 30, 201).get("id").asLong();
    number("totalOnHandQty", "0");
    request(
      "POST",
      "/receipt/api/notes/" + id + "/approve",
      Map.of("approved", true),
      staff,
      403
    );
    approveReceipt(id);
    number("totalOnHandQty", "20.50");
    request(
      "POST",
      "/receipt/api/notes/" + id + "/approve",
      Map.of("approved", true),
      manager,
      409
    );
    long out = issue("15.25", 201).get("id").asLong();
    number("totalOnHandQty", "20.50");
    number("totalReservedQty", "15.25");
    number("availableQty", "5.25");
    assertTrue(stock().get("underMin").asBoolean());
    assertEquals(
      0,
      stock()
        .get("suggestedOrderQty")
        .decimalValue()
        .compareTo(new BigDecimal("0"))
    );
    request("POST", "/issue/api/notes/" + out + "/complete", null, staff, 409);
    request("POST", "/issue/api/notes/" + out + "/approve", null, staff, 403);
    request("POST", "/issue/api/notes/" + out + "/approve", null, manager, 200);
    number("totalOnHandQty", "20.50");
    request("POST", "/issue/api/notes/" + out + "/complete", null, staff, 200);
    number("totalOnHandQty", "5.25");
    number("totalReservedQty", "0");
    request("POST", "/issue/api/notes/" + out + "/complete", null, staff, 409);
    String query =
      "?productId=" +
      product +
      "&fromDate=" +
      LocalDate.now() +
      "&toDate=" +
      LocalDate.now();
    JsonNode ledger = request(
      "GET",
      "/inventory/api/stock-cards" + query,
      null,
      staff,
      200
    );
    assertEquals(2, ledger.size());
    assertNotNull(ledger.get(1).get("performedByName").textValue());
    assertEquals(
      0,
      ledger
        .get(1)
        .get("afterQty")
        .decimalValue()
        .compareTo(new BigDecimal("5.25"))
    );
    assertEquals(
      0,
      request("GET", "/inventory/api/summary" + query, null, staff, 200)
        .get("closing")
        .decimalValue()
        .compareTo(new BigDecimal("5.25"))
    );
  }

  @Test
  void rejectsInvalidPoOverReceiptDatesAndBadGoods() throws Exception {
    receipt("OVER", "101", 20, 400);
    receipt("TODAY", "1", 0, 400);
    request(
      "POST",
      "/receipt/api/notes",
      Map.of(
        "poNumber",
        "MISSING",
        "supplierName",
        "Supplier",
        "items",
        List.of(receiptLine("A", "1", 30))
      ),
      staff,
      404
    );
    var line = receiptLine("A", "2", 20);
    line.put("rejectedQty", 1);
    request(
      "POST",
      "/receipt/api/notes",
      Map.of(
        "poNumber",
        po,
        "supplierName",
        "Supplier",
        "items",
        List.of(line)
      ),
      staff,
      400
    );
    line.put("rejectionReason", "Rách bao bì");
    long id = request(
      "POST",
      "/receipt/api/notes",
      Map.of(
        "poNumber",
        po,
        "supplierName",
        "Supplier",
        "items",
        List.of(line)
      ),
      staff,
      201
    )
      .get("id")
      .asLong();
    request(
      "POST",
      "/receipt/api/notes/" + id + "/approve",
      Map.of("approved", false, "reviewNote", "Sai chứng từ"),
      manager,
      200
    );
    number("totalOnHandQty", "0");
    receipt("RESERVE-PO", "99", 20, 201);
    receipt("PO-OVERLAP", "2", 20, 400);
  }

  @Test
  void fefoFifoOverrideCancellationAndRollback() throws Exception {
    approveReceipt(receipt("OLDER", "10", 60, 201).get("id").asLong());
    approveReceipt(receipt("SOON", "10", 10, 201).get("id").asLong());
    JsonNode fefo = request(
      "GET",
      "/issue/api/propose-fefo?productId=" +
      product +
      "&requestedQty=5&strategy=FEFO",
      null,
      staff,
      200
    );
    assertEquals("SOON", fefo.get(0).get("batchCode").asText());
    JsonNode fifo = request(
      "GET",
      "/issue/api/propose-fefo?productId=" +
      product +
      "&requestedQty=5&strategy=FIFO",
      null,
      staff,
      200
    );
    assertEquals("OLDER", fifo.get(0).get("batchCode").asText());
    Map<String, Object> data = new HashMap<>(
      Map.of(
        "orderNumber",
        "O",
        "customerName",
        "C",
        "strategy",
        "FEFO",
        "items",
        List.of(
          Map.of("productId", product, "requestedQty", 4),
          Map.of("productId", product, "requestedQty", 100)
        )
      )
    );
    request("POST", "/issue/api/notes", data, staff, 400);
    number("totalReservedQty", "0");
    data.put(
      "items",
      List.of(
        Map.of(
          "productId",
          product,
          "requestedQty",
          3,
          "selectedBatchId",
          fifo.get(0).get("batchId").asLong()
        )
      )
    );
    request("POST", "/issue/api/notes", data, staff, 400);
    data.put("overrideReason", "Theo yêu cầu sản xuất");
    long overridden = request("POST", "/issue/api/notes", data, staff, 201)
      .get("id")
      .asLong();
    request(
      "POST",
      "/issue/api/notes/" + overridden + "/reject",
      Map.of("reason", "Đổi kế hoạch"),
      manager,
      200
    );
    number("totalReservedQty", "0");
    long id = issue("5", 201).get("id").asLong();
    request(
      "POST",
      "/issue/api/notes/" + id + "/cancel",
      Map.of("reason", "Không còn nhu cầu"),
      staff,
      204
    );
    number("totalReservedQty", "0");
    number("totalOnHandQty", "20");
    request(
      "POST",
      "/issue/api/notes/" + id + "/cancel",
      Map.of("reason", "Lặp"),
      staff,
      409
    );
  }

  @Test
  void concurrentIssuesCannotReserveBeyondOnHand() throws Exception {
    approveReceipt(receipt("RACE", "10", 20, 201).get("id").asLong());
    var pool = Executors.newFixedThreadPool(2);
    CountDownLatch start = new CountDownLatch(1);
    Callable<Integer> task = () -> {
      start.await();
      var result = mvc
        .perform(
          post("/issue/api/notes")
            .session(staff)
            .contentType("application/json")
            .content(
              json.writeValueAsString(
                Map.of(
                  "orderNumber",
                  "RACE",
                  "customerName",
                  "C",
                  "strategy",
                  "FEFO",
                  "items",
                  List.of(Map.of("productId", product, "requestedQty", 7))
                )
              )
            )
        )
        .andReturn();
      return result.getResponse().getStatus();
    };
    try {
      var a = pool.submit(task);
      var b = pool.submit(task);
      start.countDown();
      var statuses = new ArrayList<>(
        List.of(a.get(20, TimeUnit.SECONDS), b.get(20, TimeUnit.SECONDS))
      );
      Collections.sort(statuses);
      assertEquals(List.of(201, 400), statuses);
      number("totalReservedQty", "7");
      number("availableQty", "3");
    } finally {
      pool.shutdownNow();
    }
  }

  @Test
  void expiredBlockedTodayAllowedAndFiltersValidated() throws Exception {
    approveReceipt(receipt("EXPIRES", "10", 20, 201).get("id").asLong());
    StockBatch b = batches.findByProductId(product).get(0);
    b.setExpDate(LocalDate.now().minusDays(1));
    batches.save(b);
    number("availableQty", "0");
    issue("1", 400);
    b.setExpDate(LocalDate.now());
    batches.save(b);
    number("availableQty", "10");
    issue("1", 201);
    request(
      "GET",
      "/inventory/api/stock-cards?productId=" +
      product +
      "&fromDate=2026-12-01&toDate=2026-01-01",
      null,
      staff,
      400
    );
    request("GET", "/inventory/api/batches?zone=INVALID", null, staff, 400);
    request(
      "GET",
      "/inventory/api/alerts/near-expiry?thresholdDays=0",
      null,
      staff,
      400
    );
  }

  @Test
  void closesPoEmptiesLocationAndPreventsReplay() throws Exception {
    long id = receipt("FULL", "100", 40, 201).get("id").asLong();
    approveReceipt(id);
    assertEquals("CLOSED", orders.findByPoNumber(po).orElseThrow().getStatus());
    receipt("MORE", "1", 40, 409);
    long out = issue("100", 201).get("id").asLong();
    request("POST", "/issue/api/notes/" + out + "/approve", null, manager, 200);
    request("POST", "/issue/api/notes/" + out + "/complete", null, staff, 200);
    number("availableQty", "0");
    number("totalReservedQty", "0");
    for (JsonNode loc : request(
      "GET",
      "/catalog/api/locations",
      null,
      staff,
      200
    )) if (loc.get("id").asLong() == location) assertEquals(
      "EMPTY",
      loc.get("status").asText()
    );
    assertEquals(
      0,
      stock()
        .get("suggestedOrderQty")
        .decimalValue()
        .compareTo(new BigDecimal("100.50"))
    );
  }

  @Test
  void receiptApprovalRollsBackEveryLineOnLotConflict() throws Exception {
    approveReceipt(receipt("SAME", "10", 20, 201).get("id").asLong());
    Map<String, Object> data = Map.of(
      "poNumber",
      po,
      "supplierName",
      "Supplier",
      "items",
      List.of(receiptLine("NEW", "3", 30), receiptLine("SAME", "4", 40))
    );
    long id = request("POST", "/receipt/api/notes", data, staff, 201)
      .get("id")
      .asLong();
    request(
      "POST",
      "/receipt/api/notes/" + id + "/approve",
      Map.of("approved", true),
      manager,
      409
    );
    number("totalOnHandQty", "10");
    assertEquals(1, batches.findByProductId(product).size());
    assertEquals(
      "PENDING_APPROVAL",
      request("GET", "/receipt/api/notes/" + id, null, staff, 200)
        .get("status")
        .asText()
    );
    JsonNode storedPo = null;
    for (JsonNode row : request(
      "GET",
      "/receipt/api/purchase-orders",
      null,
      manager,
      200
    )) if (row.get("poNumber").asText().equals(po)) storedPo = row;
    assertNotNull(storedPo);
    assertEquals(
      0,
      storedPo
        .get("items")
        .get(0)
        .get("receivedQty")
        .decimalValue()
        .compareTo(new BigDecimal("10"))
    );
  }

  @Test
  @org.junit.jupiter.api.DisplayName(
    "TC-FR02.02-P2: Nhập kho mặt hàng không quản lý hạn dùng (manage_by_lot = FALSE) với exp_date NULL, tự sinh mã lô và xuất FIFO"
  )
  void tc_fr02_02_p2_nonLotGoodsUseFifoAndDecimalFractionsAreRetained()
    throws Exception {
    var p = productRepository.findById(product).orElseThrow();
    p.setManageByLot(false);
    productRepository.save(p);
    var first = receiptLine("NO-EXP", "0.50", 30);
    first.remove("expiryDate");
    first.remove("batchCode");
    long id = request(
      "POST",
      "/receipt/api/notes",
      Map.of(
        "poNumber",
        po,
        "supplierName",
        "Supplier",
        "items",
        List.of(first)
      ),
      staff,
      201
    )
      .get("id")
      .asLong();
    approveReceipt(id);
    assertNull(batches.findByProductId(product).get(0).getExpDate());
    number("totalOnHandQty", "0.50");
    long out = issue("0.25", 201).get("id").asLong();
    request("POST", "/issue/api/notes/" + out + "/approve", null, manager, 200);
    request("POST", "/issue/api/notes/" + out + "/complete", null, staff, 200);
    number("totalOnHandQty", "0.25");
  }

  @Test
  void damagedLotsAreExcludedAndInvalidThresholdsDoNotPersist()
    throws Exception {
    approveReceipt(receipt("DAMAGED", "10", 20, 201).get("id").asLong());
    StockBatch b = batches.findByProductId(product).get(0);
    b.setStatus("DAMAGED");
    batches.save(b);
    number("availableQty", "0");
    number("totalOnHandQty", "10");
    issue("1", 400);
    request(
      "PUT",
      "/catalog/api/products/" + product + "/limits",
      Map.of("minStock", 10, "maxStock", 10),
      admin,
      400
    );
    request(
      "PUT",
      "/catalog/api/products/" + product + "/limits",
      Map.of("minStock", -1, "maxStock", 10),
      admin,
      400
    );
    request(
      "PUT",
      "/catalog/api/products/" + product + "/limits",
      Map.of(
        "minStock",
        new BigDecimal("0.25"),
        "maxStock",
        new BigDecimal("0.50")
      ),
      admin,
      200
    );
    assertEquals(
      0,
      stock()
        .get("minStockAlert")
        .decimalValue()
        .compareTo(new BigDecimal("0.25"))
    );
  }
}
