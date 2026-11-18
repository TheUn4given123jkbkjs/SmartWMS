"use strict";
const $ = (s, root = document) => root.querySelector(s);
const $$ = (s, root = document) => [...root.querySelectorAll(s)];
const page = document.body.dataset.page;
const escapeHtml = (v) =>
  String(v ?? "Chưa có dữ liệu").replace(
    /[&<>"']/g,
    (c) =>
      ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[
        c
      ],
  );
const num = (v) =>
  v == null ? "Chưa có dữ liệu" : new Intl.NumberFormat("vi-VN").format(v);
const date = (v) => {
  if (!v) return "Chưa có dữ liệu";
  const [d, time] = v.split("T");
  return (
    d.split("-").reverse().join("/") + (time ? " " + time.slice(0, 5) : "")
  );
};
const labels = {
  PENDING_APPROVAL: "Chờ duyệt",
  APPROVED: "Đã duyệt",
  APPROVED_READY_TO_PICK: "Đã duyệt — chờ bốc",
  PICKING: "Đang bốc",
  COMPLETED: "Hoàn tất",
  CANCELLED: "Đã hủy",
  REJECTED: "Từ chối",
  EMPTY: "Trống",
  OCCUPIED: "Đang chứa",
  WARNING: "Cận hạn",
  CRITICAL: "Cận hạn nguy cấp",
  EXPIRED: "Quá hạn",
  NHAP_PO: "Nhập PO",
  XUAT_KHO: "Xuất kho",
};
const badge = (v) =>
  `<span class="badge-state ${["EXPIRED", "REJECTED"].includes(v) ? "danger" : ["PENDING_APPROVAL", "WARNING", "CRITICAL"].includes(v) ? "warning" : ["EMPTY", "COMPLETED"].includes(v) ? "success" : ""}">${escapeHtml(labels[v] || v)}</span>`;
let purchaseOrders = [],
  batchRows = [];
let user,
  products = [],
  locations = [],
  records = [],
  stock = [],
  revision = 0;
function feedback(message) {
  $("#feedback").textContent = message;
}
async function api(url, options = {}) {
  const res = await fetch(url, {
    ...options,
    headers: { "Content-Type": "application/json", ...options.headers },
  });
  const text = await res.text();
  let body;
  try {
    body = text ? JSON.parse(text) : null;
  } catch {
    body = null;
  }
  if (!res.ok) {
    if (res.status === 401) {
      feedback("Phiên đã hết hạn. Đang chuyển đến đăng nhập…");
      location.assign("/auth/login");
    }
    const e = new Error(
      body?.message ||
        body?.error ||
        `Không thực hiện được yêu cầu (${res.status}).`,
    );
    e.fields = body?.validationErrors;
    throw e;
  }
  return body;
}
function options(rows, key = "id", label = "name") {
  return (
    '<option value="">Chọn…</option>' +
    rows
      .map(
        (r) =>
          `<option value="${escapeHtml(r[key])}">${escapeHtml(r[label])}</option>`,
      )
      .join("")
  );
}
function table(target, rows, columns) {
  const root = typeof target === "string" ? $(target) : target;
  let current = 0;
  function draw() {
    if (!rows.length) {
      root.innerHTML =
        '<div class="empty"><i class="bi bi-inbox fs-3"></i><p class="mt-2 mb-0">Không có dữ liệu phù hợp.</p></div>';
      return;
    }
    const count = Math.ceil(rows.length / 10);
    root.innerHTML =
      '<div class="table-wrap"><table class="work-table"><thead><tr>' +
      columns.map((c) => `<th scope="col">${escapeHtml(c[0])}</th>`).join("") +
      "</tr></thead><tbody>" +
      rows
        .slice(current * 10, current * 10 + 10)
        .map(
          (r) =>
            "<tr>" +
            columns
              .map(
                (c) =>
                  `<td>${typeof c[1] === "function" ? c[1](r) : escapeHtml(r[c[1]])}</td>`,
              )
              .join("") +
            "</tr>",
        )
        .join("") +
      "</tbody></table></div>" +
      `<div class="pagination-bar"><span>${rows.length} kết quả · Trang ${current + 1}/${count}</span><div><button class="btn btn-sm btn-light" data-prev ${current === 0 ? "disabled" : ""}>Trước</button> <button class="btn btn-sm btn-light" data-next ${current === count - 1 ? "disabled" : ""}>Sau</button></div></div>`;
    $("[data-prev]", root).onclick = () => {
      current--;
      draw();
    };
    $("[data-next]", root).onclick = () => {
      current++;
      draw();
    };
  }
  draw();
}
const stockCols = [
  ["SKU", "skuCode"],
  ["Mặt hàng", "productName"],
  ["ĐVT", "uom"],
  ["Thực tế", (r) => num(r.totalOnHandQty)],
  ["Giữ chỗ", (r) => num(r.totalReservedQty)],
  ["Khả dụng", (r) => num(r.availableQty)],
];
const pickCols = [
  ["SKU", "skuCode"],
  ["Số lô", "batchCode"],
  ["Vị trí", "locationCode"],
  ["Hạn dùng", (r) => date(r.expiryDate)],
  ["SL bốc", (r) => num(r.allocatedQty)],
];
function match(r) {
  return (
    !$("#search")?.value ||
    JSON.stringify(r)
      .toLocaleLowerCase("vi")
      .includes($("#search").value.toLocaleLowerCase("vi"))
  );
}
function showDetail(title, html) {
  $("#detailTitle").textContent = title;
  $("#detailBody").innerHTML = html;
  $("#detailDialog").showModal();
}
function confirmAction(message) {
  return new Promise((resolve) => {
    const d = $("#confirmDialog");
    $("#confirmText").textContent = message;
    const close = (v) => {
      d.close();
      resolve(v);
    };
    $("[data-confirm]", d).onclick = () => close(true);
    $("[data-cancel]", d).onclick = () => close(false);
    d.oncancel = (e) => {
      e.preventDefault();
      close(false);
    };
    d.showModal();
  });
}
async function load() {
  const rev = ++revision;
  feedback("");
  $$(".data-region").forEach(
    (r) => (r.innerHTML = '<div class="empty">Đang tải dữ liệu…</div>'),
  );
  try {
    if (page === "products") {
      [records, window.categories] = await Promise.all([
        api("/catalog/api/products"),
        api("/catalog/api/categories"),
      ]);
      $("#categoryId").innerHTML = options(window.categories);
      $("#categoryFilter").innerHTML =
        '<option value="">Tất cả nhóm</option>' +
        window.categories
          .map((c) => `<option>${escapeHtml(c.name)}</option>`)
          .join("");
      renderProducts();
    }
    if (page === "locations") {
      records = await api("/catalog/api/locations");
      renderLocations();
    }
    if (["dashboard", "alerts"].includes(page)) {
      const days = Number($("#thresholdDays")?.value || 30);
      if (days < 1 || days > 365) throw new Error("Số ngày phải từ 1 đến 365.");
      const [low, expiry, ...metrics] = await Promise.all([
        api("/inventory/api/alerts/low-stock"),
        api("/inventory/api/alerts/near-expiry?thresholdDays=" + days),
        ...(page === "dashboard"
          ? [
              api("/catalog/api/products"),
              api("/receipt/api/notes?status=PENDING_APPROVAL"),
              api("/issue/api/notes?status=PENDING_APPROVAL"),
            ]
          : []),
      ]);
      if (rev !== revision) return;
      records = { low, expiry };
      renderAlerts();
      if (page === "dashboard") {
        $("#metrics").innerHTML =
          '<div class="metrics">' +
          [
            ["Mã hàng đang quản lý", metrics[0].length, "/catalog/products"],
            ["Phiếu nhập chờ duyệt", metrics[1].length, "/receipt/approve"],
            ["Lệnh xuất chờ xử lý", metrics[2].length, "/issue/pick"],
            [
              "Cảnh báo cần xem",
              low.length + expiry.length,
              "/inventory/alerts",
            ],
          ]
            .map(
              ([s, n, url]) =>
                `<a class="metric" href="${url}"><span>${s}</span><strong>${num(n)}</strong><small>Xem chi tiết →</small></a>`,
            )
            .join("") +
          "</div>";
      }
    }
    if (["approve", "pick"].includes(page)) {
      records = await api(
        page === "approve" ? "/receipt/api/notes" : "/issue/api/notes",
      );
      renderNotes();
    }
    if (["receipt", "issue", "stock"].includes(page)) {
      [products, locations, stock] = await Promise.all([
        api("/catalog/api/products"),
        api("/catalog/api/locations"),
        api("/inventory/api/available-stock"),
      ]);
      if (page === "stock") {
        $("#productId").innerHTML = options(products);
        const selected = new URLSearchParams(location.search).get("productId");
        if (selected) $("#productId").value = selected;
        table("#stock", stock, stockCols);
        const today = new Date();
        $("#toDate").value = localDay(today);
        today.setDate(1);
        $("#fromDate").value = localDay(today);
        if (selected) await history();
        const categories = await api("/catalog/api/categories");
        $("#batchCategory").innerHTML =
          '<option value="">Tất cả nhóm</option>' +
          categories
            .map(
              (c) => `<option value="${c.id}">${escapeHtml(c.name)}</option>`,
            )
            .join("");
        await filterBatches();
      } else {
        if (page === "receipt") {
          purchaseOrders = await api("/receipt/api/purchase-orders");
          const previous = $("#poNumber").value;
          $("#poNumber").innerHTML = options(
            purchaseOrders.filter((p) => p.status === "APPROVED"),
            "poNumber",
            "poNumber",
          );
          if (previous) $("#poNumber").value = previous;
          $("#poImport").hidden = user.role !== "ROLE_MANAGER";
          if (!$("#poLines").children.length) addPoLine();
        } else batchRows = await api("/inventory/api/batches");
        if (!$("#lines").children.length) addLine();
      }
    }
    $("#updated").textContent =
      "Cập nhật " + new Date().toLocaleTimeString("vi-VN");
  } catch (e) {
    if (rev !== revision) return;
    feedback(e.message);
    $$(".data-region").forEach((r) => {
      r.innerHTML =
        '<div class="empty">Không tải được dữ liệu. <button type="button" class="btn btn-sm btn-light" data-retry>Thử lại</button></div>';
    });
  }
}
function renderProducts() {
  table(
    "#table",
    records.filter(
      (r) =>
        match(r) &&
        (!$("#categoryFilter").value ||
          r.categoryName === $("#categoryFilter").value) &&
        (!$("#activeFilter").value ||
          String(r.active) === $("#activeFilter").value),
    ),
    [
      ["SKU", "skuCode"],
      ["Tên hàng", "name"],
      ["Nhóm", "categoryName"],
      ["ĐVT", "uom"],
      ["Min", (r) => num(r.minStock)],
      ["Max", (r) => num(r.maxStock)],
      ["Theo lô", (r) => (r.manageByLot ? "Có" : "Không")],
      ["Trạng thái", (r) => (r.active ? "Hoạt động" : "Ngừng hoạt động")],
      [
        "Định mức",
        (r) =>
          ["ROLE_MANAGER", "ROLE_ADMIN"].includes(user.role)
            ? `<button class="btn btn-sm btn-light" data-limits="${r.id}">Thiết lập Min/Max</button>`
            : "Chỉ xem",
      ],
    ],
  );
}
function renderLocations() {
  const rows = records.filter(
    (r) =>
      match(r) &&
      (!$("#zone").value || r.zone === $("#zone").value) &&
      (!$("#locationStatus").value || r.status === $("#locationStatus").value),
  );
  window.WarehouseMap.render(records, rows);
}

function renderAlerts() {
  if (!records.low || !records.expiry) return;
  table("#low", records.low.filter(match), [
    ...(page === "dashboard"
      ? [
          [
            "Mặt hàng",
            (r) =>
              `<strong>${escapeHtml(r.skuCode)}</strong><br>${escapeHtml(r.productName)}`,
          ],
          ["Khả dụng", (r) => num(r.availableQty)],
        ]
      : stockCols),
    ["Min", (r) => num(r.minStockAlert)],
    ["Max", (r) => num(r.maxStock)],
    ["Đang về", (r) => num(r.onOrder)],
    [
      "Gợi ý đặt",
      (r) =>
        Number(r.suggestedOrderQty) === 0 && Number(r.onOrder) > 0
          ? `${num(r.suggestedOrderQty)}<br><small class="muted">Đã có hàng đang về</small>`
          : num(r.suggestedOrderQty),
    ],
    [
      "Thao tác",
      (r) =>
        `<a href="/inventory/stock-card?productId=${r.productId}">Thẻ kho</a><br><a href="/receipt/create?productId=${r.productId}">Lập phiếu nhập</a>`,
    ],
  ]);
  table("#expiry", records.expiry.filter(match), [
    ["SKU", "skuCode"],
    ["Mặt hàng", "productName"],
    ["Lô", "batchCode"],
    ["Vị trí", "locationCode"],
    ["EXP", (r) => date(r.expiryDate)],
    ["Còn (ngày)", (r) => num(r.daysRemaining)],
    ["Số lượng", (r) => num(r.currentQty)],
    ["Mức cảnh báo", (r) => badge(r.alertLevel)],
  ]);
}
function renderNotes() {
  table(
    "#table",
    records.filter(
      (r) =>
        match(r) && (!$("#status").value || r.status === $("#status").value),
    ),
    [
      [
        page === "approve" ? "Phiếu nhập" : "Lệnh xuất",
        (r) =>
          `<button class="btn btn-sm btn-link" data-note="${r.id}">${escapeHtml(r.receiptCode || r.issueCode)}</button>${r.isOverridden ? ' <span class="badge-state warning">Ngoại lệ FEFO/FIFO</span>' : ""}`,
      ],
      ["PO / Đơn hàng", (r) => escapeHtml(r.poNumber || r.orderNumber)],
      ["Đối tác", (r) => escapeHtml(r.supplierName || r.customerName)],
      ["Người lập", "makerName"],
      ["Ngày tạo", (r) => date(r.createdAt)],
      ["Trạng thái", (r) => badge(r.status)],
    ],
  );
}
function localDay(d) {
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}-${String(d.getDate()).padStart(2, "0")}`;
}
let historyVersion = 0;
async function history() {
  const version = ++historyVersion;
  const id = $("#productId").value,
    from = $("#fromDate").value,
    to = $("#toDate").value;
  if (!id) return;
  if (from > to) {
    $("#toDate").setCustomValidity(
      "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu.",
    );
    $("#toDate").reportValidity();
    return;
  }
  $("#toDate").setCustomValidity("");
  $("#history").textContent = "Đang tải lịch sử…";
  try {
    const rows = await api(
      `/inventory/api/stock-cards?productId=${id}&fromDate=${from}&toDate=${to}`,
    );
    if (version !== historyVersion) return;
    table("#history", rows, [
      ["Thời điểm", (r) => date(r.transactionTime)],
      ["Loại", (r) => badge(r.changeType)],
      ["Chứng từ", "referenceCode"],
      ["SKU", "skuCode"],
      ["Lô", "batchCode"],
      ["Vị trí", "locationCode"],
      ["Tồn trước", (r) => num(r.beforeQty)],
      ["Biến động", (r) => num(r.changeQty)],
      ["Tồn sau", (r) => num(r.afterQty)],
      ["Người thực hiện", "performedByName"],
      ["Người duyệt", "approvedByName"],
      ["Diễn giải", "reason"],
    ]);
    table(
      "#stock",
      stock.filter((r) => String(r.productId) === id),
      stockCols,
    );
    const summary = await api(
      `/inventory/api/summary?productId=${id}&fromDate=${from}&toDate=${to}`,
    );
    $("#summary").innerHTML =
      '<div class="metrics">' +
      [
        ["Tồn đầu kỳ", summary.opening],
        ["Tổng nhập", summary.received],
        ["Tổng xuất", summary.issued],
        ["Tồn cuối kỳ", summary.closing],
      ]
        .map(
          ([label, value]) =>
            `<div class="metric"><span>${label}</span><strong>${num(value)}</strong></div>`,
        )
        .join("") +
      "</div>";
  } catch (e) {
    $("#history").textContent = e.message;
  }
}
function addLine() {
  if ($("#propose")) $("#propose").dataset.stale = "true";
  const index = Date.now() + Math.random().toString(36).slice(2);
  const inbound = page === "receipt";
  const div = document.createElement("section");
  div.className = "line";
  const input = (n, label, type = "text", attrs = "") =>
    `<div class="field"><label for="${n}${index}">${label}</label><input id="${n}${index}" data-field="${n}" type="${type}" class="form-control" ${attrs}></div>`;
  div.innerHTML =
    '<div class="line-head"><strong>Mặt hàng</strong><button type="button" class="btn btn-sm btn-outline-danger" data-remove>Xóa dòng</button></div><div class="form-grid">' +
    `<div class="field"><label for="product${index}">SKU *</label><select id="product${index}" class="form-select" data-field="productId" required>${options(products.map((p) => ({ ...p, name: p.skuCode + " · " + p.name })))}</select></div>` +
    (inbound
      ? input(
          "plannedQty",
          "PO còn được nhận",
          "number",
          'required min="0.01" step="0.01"',
        ) +
        input(
          "actualQty",
          "SL thực nhận *",
          "number",
          'required min="0.01" step="0.01"',
        ) +
        input("batchCode", "Số lô", "text") +
        input("manufactureDate", "Ngày sản xuất", "date") +
        input("expiryDate", "Hạn dùng", "date") +
        `<div class="field"><label for="location${index}">Vị trí *</label><select id="location${index}" data-field="locationId" class="form-select" required>${options(
          locations.filter(
            (l) => ["RAW", "FG"].includes(l.zone) && l.status !== "MAINTENANCE",
          ),
          "id",
          "locationCode",
        )}</select></div>` +
        input(
          "rejectedQty",
          "SL từ chối",
          "number",
          'min="0" step="0.01" value="0"',
        ) +
        input("rejectionReason", "Lý do hàng lỗi")
      : input(
          "requestedQty",
          "SL yêu cầu *",
          "number",
          'required min="0.01" step="0.01"',
        )) +
    '</div><p class="line-stock small muted mt-3 mb-0">Chọn SKU để xem đơn vị tính và tồn kho.</p>';
  $("#lines").append(div);
  if (page === "receipt") {
    const po = purchaseOrders.find((p) => p.poNumber === $("#poNumber").value);
    $("[data-field=productId]", div).innerHTML = options(
      products
        .filter((p) => po?.items.some((i) => i.productId === p.id))
        .map((p) => ({ ...p, name: p.skuCode + " · " + p.name })),
    );
    $("[data-field=plannedQty]", div).readOnly = true;
  } else {
    const f = document.createElement("div");
    f.className = "field";
    f.innerHTML = `<label for="batch${index}">Chọn lô ngoại lệ (tùy chọn)</label><select id="batch${index}" class="form-select" data-field="selectedBatchId"><option value="">Theo gợi ý tự động</option></select>`;
    $(".form-grid", div).append(f);
  }
  const initial = new URLSearchParams(location.search).get("productId");
  if (initial && $("#lines").children.length === 1) {
    $("[data-field=productId]", div).value = initial;
  }
  updateLine(div);
}
function updateLine(line) {
  const id = $("[data-field=productId]", line).value,
    p = products.find((p) => String(p.id) === id),
    s = stock.find((s) => String(s.productId) === id);
  $(".line-stock", line).textContent = p
    ? `ĐVT: ${p.uom} · Thực tế: ${num(s?.totalOnHandQty)} · Giữ chỗ: ${num(s?.totalReservedQty)} · Khả dụng: ${num(s?.availableQty)}`
    : "Chọn SKU để xem đơn vị tính và tồn kho.";
  if (page === "receipt") {
    const po = purchaseOrders.find((p) => p.poNumber === $("#poNumber").value);
    const item = po?.items.find((i) => String(i.productId) === id);
    $("[data-field=plannedQty]", line).value = item?.remainingQty ?? "";
  } else if (line.dataset.product !== id) {
    const select = $("[data-field=selectedBatchId]", line);
    select.innerHTML =
      '<option value="">Theo gợi ý tự động</option>' +
      batchRows
        .filter((b) => String(b.productId) === id && b.availableQty > 0)
        .map(
          (b) =>
            `<option value="${b.batchId}">${escapeHtml(b.batchCode)} · ${escapeHtml(b.locationCode)} · ${num(b.availableQty)}</option>`,
        )
        .join("");
  }
  line.dataset.product = id;
  if (page === "receipt") {
    for (const name of ["batchCode", "expiryDate"])
      $(`[data-field=${name}]`, line).required = !!p?.manageByLot;
  }
  if (page === "issue" && p && !p.manageByLot) {
    $(".line-stock", line).textContent +=
      " · Hàng không quản lý hạn dùng: tự động FIFO.";
  }
  if ($("#proposal"))
    $("#proposal").textContent =
      "Thông tin đã thay đổi. Vui lòng lấy lại đề xuất.";
}
function validateDocument() {
  const f = $("#documentForm");
  $$("input", f).forEach((i) => i.setCustomValidity(""));
  if (!$("#lines").children.length) {
    feedback("Cần ít nhất một dòng hàng.");
    return false;
  }
  for (const line of $$("#lines .line")) {
    const get = (n) => $(`[data-field=${n}]`, line);
    if (page === "receipt") {
      if (+get("actualQty").value > +get("plannedQty").value)
        get("actualQty").setCustomValidity(
          "Thực nhận không được vượt số lượng PO.",
        );
      if (
        get("expiryDate").value &&
        get("expiryDate").value <= localDay(new Date())
      )
        get("expiryDate").setCustomValidity(
          "Hạn dùng nhập kho phải sau hôm nay.",
        );
      if (
        get("manufactureDate").value &&
        get("expiryDate").value &&
        get("manufactureDate").value > get("expiryDate").value
      )
        get("manufactureDate").setCustomValidity(
          "Ngày sản xuất không được sau hạn dùng.",
        );
    } else {
      const id = get("productId").value;
      const total = $$("#lines .line")
        .filter((l) => $("[data-field=productId]", l).value === id)
        .reduce(
          (s, l) => s + Number($("[data-field=requestedQty]", l).value),
          0,
        );
      const available = stock.find(
        (s) => String(s.productId) === id,
      )?.availableQty;
      if (
        available != null &&
        Math.round(total * 100) > Math.round(Number(available) * 100)
      )
        get("requestedQty").setCustomValidity(
          "Tổng yêu cầu của SKU vượt tồn khả dụng.",
        );
    }
  }
  return f.reportValidity();
}
async function propose() {
  if (!validateDocument()) return;
  const button = $("#propose");
  button.disabled = true;
  $("#proposal").textContent = "Đang tính đề xuất…";
  try {
    const grouped = new Map();
    for (const line of $$("#lines .line")) {
      const id = $("[data-field=productId]", line).value;

      grouped.set(
        id,
        (Math.round((grouped.get(id) || 0) * 100) +
          Math.round(
            Number($("[data-field=requestedQty]", line).value) * 100,
          )) /
          100,
      );
    }
    const rows = (
      await Promise.all(
        [...grouped].map(([id, qty]) =>
          api(
            `/issue/api/propose-fefo?productId=${id}&requestedQty=${qty}&strategy=${$("#strategy").value}`,
          ),
        ),
      )
    ).flat();
    if (button.dataset.stale === "true") {
      button.dataset.stale = "false";
      $("#proposal").textContent =
        "Thông tin đã thay đổi. Vui lòng lấy lại đề xuất.";
      return;
    }
    table("#proposal", rows, pickCols);
  } catch (e) {
    $("#proposal").textContent = e.message;
  } finally {
    button.disabled = false;
  }
}
async function saveCatalog(event) {
  event.preventDefault();
  const form = event.target;
  $$("[data-error]", form).forEach((e) => (e.textContent = ""));
  $("#formError").textContent = "";
  if (page === "products") {
    $("#maxStock").setCustomValidity(
      Number($("#maxStock").value) <= Number($("#minStock").value)
        ? "Max phải lớn hơn Min."
        : "",
    );
    if (!form.reportValidity()) return;
  }
  if (!(await confirmAction("Lưu thông tin mới vào danh mục kho?"))) return;
  const button = $("[type=submit]", form);
  button.disabled = true;
  const data = Object.fromEntries(new FormData(form));
  if (page === "products") {
    for (const k of ["categoryId", "minStock", "maxStock"])
      data[k] = Number(data[k]);
    data.manageByLot = data.manageByLot === "true";
  } else {
    data.zone = data.zoneInput;
    delete data.zoneInput;
    data.maxCapacity = Number(data.maxCapacity);
  }
  try {
    await api(
      page === "products" ? "/catalog/api/products" : "/catalog/api/locations",
      { method: "POST", body: JSON.stringify(data) },
    );
    $("#editDialog").close();
    form.reset();
    await load();
    feedback("Đã lưu thông tin danh mục.");
  } catch (e) {
    $("#formError").textContent = e.message;
    Object.entries(e.fields || {}).forEach(([key, value]) => {
      const el = $$("[data-error]", form).find(
        (el) => el.dataset.error === key,
      );
      if (el) el.textContent = value;
    });
  } finally {
    button.disabled = false;
  }
}
document.addEventListener("click", async (e) => {
  const b = e.target.closest("button");
  if (!b) return;
  try {
    if (b.hasAttribute("data-close")) b.closest("dialog").close();
    if (b.hasAttribute("data-retry") || b.id === "refresh") await load();
    if (b.id === "add") $("#editDialog").showModal();
    if (b.id === "addLine") addLine();
    if (b.hasAttribute("data-remove")) {
      if ($("#propose")) $("#propose").dataset.stale = "true";
      b.closest(".line").remove();
      if ($("#proposal"))
        $("#proposal").textContent =
          "Thông tin đã thay đổi. Vui lòng lấy lại đề xuất.";
    }
    if (b.id === "validate") {
      $("#documentFeedback").textContent = validateDocument()
        ? "Thông tin nhập hợp lệ ở mức biểu mẫu. Chứng từ chưa được lưu."
        : "";
    }
    if (b.id === "sendDocument") await sendDocument(b);
    if (b.id === "addPoLine") addPoLine();
    if (b.id === "filterBatches") await filterBatches();
    if (b.dataset.limits) {
      const row = records.find((r) => String(r.id) === b.dataset.limits);
      showDetail(
        "Định mức · " + row.skuCode,
        `<form id="limitsForm" data-id="${row.id}"><label for="limitMin">Tồn tối thiểu</label><input class="form-control" type="number" id="limitMin" min="0" step="0.01" required value="${row.minStock}"><label for="limitMax">Tồn tối đa</label><input class="form-control" type="number" id="limitMax" min="0.01" step="0.01" required value="${row.maxStock}"><button class="btn btn-orange-primary mt-3" type="submit">Lưu định mức</button><p id="limitsError" role="alert"></p></form>`,
      );
    }
    if (b.dataset.noteAction) await noteAction(b);
    if (b.id === "propose") {
      b.dataset.stale = "false";
      await propose();
    }
    if (b.dataset.location) {
      const r = records.find((r) => String(r.id) === b.dataset.location);
      const rows = (
        await api(
          "/inventory/api/batches?keyword=" +
            encodeURIComponent(r.locationCode),
        )
      ).filter((b) => b.locationId === r.id);
      showDetail(
        r.locationCode,
        `<p>Khu vực: ${escapeHtml(r.zone)} · ${badge(r.status)}</p><p>Dãy / Kệ / Tầng / Ô: ${[r.aisle, r.rack, r.shelf, r.bin].map((x) => escapeHtml(x || "—")).join(" / ")}</p><p>Sức chứa thiết kế: ${num(r.maxCapacity)}</p><div id="locationBatches"></div>`,
      );
      table("#locationBatches", rows, [
        ["SKU", "skuCode"],
        ["Tên", "productName"],
        ["Lô", "batchCode"],
        ["EXP", (r) => date(r.expiryDate)],
        ["Thực tế", (r) => num(r.onHandQty)],
        ["Giữ chỗ", (r) => num(r.reservedQty)],
        ["Khả dụng", (r) => num(r.availableQty)],
      ]);
    }
    if (b.dataset.note) await openNote(b.dataset.note);
    if (b.hasAttribute("data-print")) window.print();
  } catch (err) {
    feedback(err.message);
  }
});
$("#menuToggle")?.addEventListener("click", () => {
  const open = $("#sidebar").classList.toggle("open");
  $("#menuToggle").setAttribute("aria-expanded", open);
});
document.addEventListener("keydown", (e) => {
  if (e.key === "Escape") {
    $("#sidebar")?.classList.remove("open");
    $("#menuToggle")?.setAttribute("aria-expanded", "false");
  }
});
document.addEventListener("click", (e) => {
  if (!e.target.closest("#sidebar,#menuToggle")) {
    $("#sidebar")?.classList.remove("open");
    $("#menuToggle")?.setAttribute("aria-expanded", "false");
  }
});
$("#editForm")?.addEventListener("submit", saveCatalog);
$("#maxStock")?.addEventListener("input", () =>
  $("#maxStock").setCustomValidity(""),
);
$("#toDate")?.addEventListener("input", () =>
  $("#toDate").setCustomValidity(""),
);
$("#filterForm")?.addEventListener("submit", (e) => {
  e.preventDefault();
  history();
});
$("#documentForm")?.addEventListener("submit", (e) => e.preventDefault());
$("#lines")?.addEventListener("input", (e) => {
  updateLine(e.target.closest(".line"));
  if ($("#propose")) $("#propose").dataset.stale = "true";
});
for (const id of [
  "search",
  "categoryFilter",
  "activeFilter",
  "zone",
  "locationStatus",
  "status",
])
  $("#" + id)?.addEventListener("input", () => {
    if (page === "products") renderProducts();
    if (page === "locations") renderLocations();
    if (["dashboard", "alerts"].includes(page)) renderAlerts();
    if (["approve", "pick"].includes(page)) renderNotes();
  });
(async () => {
  try {
    user = await api("/auth/api/me");
    if ($("#add") && !["ROLE_MANAGER", "ROLE_ADMIN"].includes(user.role)) {
      $("#add").disabled = true;
      $("#add").title = "Chỉ Quản lý kho và Quản trị viên được tạo danh mục.";
    }
    if ($("#sendDocument")) {
      $("#sendDocument").disabled = user.role !== "ROLE_STAFF";
      $("#sendDocument").title =
        user.role === "ROLE_STAFF"
          ? ""
          : "Chỉ nhân viên kho được lập chứng từ.";
    }
    await load();
    const id = new URLSearchParams(location.search).get("id");
    if (id && ["approve", "pick"].includes(page)) await openNote(id);
  } catch (e) {
    feedback(e.message);
  }
})();

$("#minStock")?.addEventListener("input", () =>
  $("#maxStock").setCustomValidity(""),
);

async function filterBatches() {
  const query = new URLSearchParams({
    keyword: $("#batchKeyword").value,
    zone: $("#batchZone").value,
    status: $("#batchStatus").value,
  });
  const category = $("#batchCategory").value;
  if (category) query.set("categoryId", category);
  const rows = await api("/inventory/api/batches?" + query);
  table("#batchTable", rows, [
    ["SKU", "skuCode"],
    ["Tên", "productName"],
    ["Lô", "batchCode"],
    ["EXP", (r) => date(r.expiryDate)],
    ["Vị trí", "locationCode"],
    ["Khu", "zone"],
    ["Trạng thái", (r) => badge(r.status)],
    ["Thực tế", (r) => num(r.onHandQty)],
    ["Giữ chỗ", (r) => num(r.reservedQty)],
    ["Khả dụng", (r) => num(r.availableQty)],
  ]);
}
function addPoLine() {
  const index = Date.now() + Math.random();
  const div = document.createElement("div");
  div.className = "line";
  div.innerHTML = `<label for="poProduct${index}">SKU</label><select id="poProduct${index}" class="form-select" data-po-product required>${options(products.map((p) => ({ ...p, name: p.skuCode + " · " + p.name })))}</select><label for="poQty${index}">Số lượng đặt</label><input id="poQty${index}" data-po-qty class="form-control" type="number" min="0.01" step="0.01" required><button type="button" class="btn btn-sm btn-light" data-remove-po>Xóa dòng PO</button>`;
  $("#poLines").append(div);
}
async function sendDocument(button) {
  if (
    !validateDocument() ||
    !(await confirmAction(
      page === "receipt"
        ? "Gửi phiếu nhập chờ quản lý duyệt?"
        : "Tạo lệnh xuất và giữ chỗ hàng hóa?",
    ))
  )
    return;
  button.disabled = true;
  try {
    const data = Object.fromEntries(new FormData($("#documentForm")));
    data.items = $$("#lines .line").map((line) =>
      Object.fromEntries(
        $$("[data-field]", line).map((el) => [
          el.dataset.field,
          el.value || null,
        ]),
      ),
    );
    const numeric = [
      "productId",
      "locationId",
      "actualQty",
      "plannedQty",
      "requestedQty",
      "selectedBatchId",
      "unitPrice",
      "rejectedQty",
    ];
    data.items.forEach((i) =>
      numeric.forEach((k) => {
        if (i[k] != null) i[k] = Number(i[k]);
      }),
    );
    const result = await api(
      page === "receipt" ? "/receipt/api/notes" : "/issue/api/notes",
      { method: "POST", body: JSON.stringify(data) },
    );
    $("#documentFeedback").textContent =
      "Đã tạo " +
      (result.receiptCode || result.issueCode) +
      (page === "issue" ? " và giữ chỗ." : " — chờ duyệt.");
    const link = document.createElement("a");
    link.href =
      (page === "receipt" ? "/receipt/approve" : "/issue/pick") +
      "?id=" +
      result.id;
    link.textContent = " Xem chứng từ →";
    $("#documentFeedback").append(link);
    $("#lines").replaceChildren();
    await load();
  } catch (e) {
    feedback(e.message);
  } finally {
    button.disabled = user.role !== "ROLE_STAFF";
  }
}
async function openNote(id) {
  const inbound = page === "approve",
    r = await api(`/${inbound ? "receipt" : "issue"}/api/notes/${id}`);
  const canApprove =
    user.role === "ROLE_MANAGER" &&
    r.makerId !== user.id &&
    r.status === "PENDING_APPROVAL";
  const canComplete =
    !inbound &&
    user.role === "ROLE_STAFF" &&
    r.status === "APPROVED_READY_TO_PICK";
  const canCancel =
    !inbound &&
    ["PENDING_APPROVAL", "APPROVED_READY_TO_PICK"].includes(r.status) &&
    (r.makerId === user.id || user.role === "ROLE_MANAGER");
  showDetail(
    r.receiptCode || r.issueCode,
    `<p>${badge(r.status)}</p><p>Người lập: ${escapeHtml(r.makerName)} · Người duyệt: ${escapeHtml(r.checkerName)}</p><p>Ghi chú: ${escapeHtml(r.note || "")}</p>${r.isOverridden ? `<p><span class="badge-state warning">Ngoại lệ FEFO/FIFO</span> ${escapeHtml(r.overrideReason || "—")}</p>` : ""}<p>Lý do quyết định: ${escapeHtml(r.decisionReason || "—")}</p><p>Xác nhận bàn giao: ${escapeHtml(r.confirmedByName || "—")} · ${date(r.completedAt)}</p><div id="detailTable"></div><label for="decisionReason">Ý kiến / lý do từ chối hoặc hủy</label><input id="decisionReason" class="form-control" maxlength="255"><p class="muted mt-3">${r.makerId === user.id ? "Người lập không được tự duyệt. " : ""}${inbound ? "Chấp thuận sẽ ghi tăng tồn." : "Duyệt giữ nguyên tồn; xác nhận bàn giao mới trừ tồn và giải phóng giữ chỗ."}</p><div class="quick-actions"><button class="btn btn-success" data-note-action="approve" data-id="${id}" ${canApprove ? "" : "disabled"}>Chấp thuận</button><button class="btn btn-outline-danger" data-note-action="reject" data-id="${id}" ${canApprove ? "" : "disabled"}>Từ chối</button>${!inbound ? `<button class="btn btn-outline-danger" data-note-action="cancel" data-id="${id}" ${canCancel ? "" : "disabled"}>Hủy phiếu</button><button class="btn btn-orange-primary" data-note-action="complete" data-id="${id}" ${canComplete ? "" : "disabled"}>Xác nhận bàn giao</button><button class="btn btn-light" data-print>In Pick List</button>` : ""}</div><p id="decisionError" class="text-danger" role="alert"></p>`,
  );
  table(
    "#detailTable",
    inbound ? r.items : r.pickList,
    inbound
      ? [
          ["SKU (ID)", "productId"],
          ["Lô", "batchCode"],
          ["Vị trí (ID)", "locationId"],
          ["SL đạt", (r) => num(r.actualQty)],
          ["SL từ chối", (r) => num(r.rejectedQty)],
          ["Lý do", "rejectionReason"],
          ["EXP", (r) => date(r.expiryDate)],
        ]
      : pickCols,
  );
}
async function noteAction(button) {
  const action = button.dataset.noteAction,
    reason = $("#decisionReason").value;
  if (["reject", "cancel"].includes(action) && !reason.trim()) {
    $("#decisionReason").setCustomValidity("Phải nhập lý do.");
    $("#decisionReason").reportValidity();
    return;
  }
  if (
    !(await confirmAction(
      "Xác nhận " +
        {
          approve: "phê duyệt",
          reject: "từ chối",
          cancel: "hủy",
          complete: "bàn giao hàng và trừ tồn",
        }[action] +
        "?",
    ))
  )
    return;
  button.disabled = true;
  try {
    let url = `/${page === "approve" ? "receipt" : "issue"}/api/notes/${button.dataset.id}/`,
      body = { reason };
    if (page === "approve") {
      url += "approve";
      body = { approved: action === "approve", reviewNote: reason };
    } else url += action;
    await api(url, { method: "POST", body: JSON.stringify(body) });
    $("#detailDialog").close();
    await load();
    feedback("Đã cập nhật chứng từ.");
  } catch (e) {
    $("#decisionError").textContent = e.message;
    button.disabled = false;
  }
}
$("#poNumber")?.addEventListener("change", () => {
  const po = purchaseOrders.find((p) => p.poNumber === $("#poNumber").value);
  $("#supplierName").value = po?.supplierName || "";
  $("#lines").replaceChildren();
  addLine();
});
$("#strategy")?.addEventListener("change", () => {
  $("#proposal").textContent =
    "Chiến lược đã thay đổi. Vui lòng lấy lại đề xuất.";
  $("#propose").dataset.stale = "true";
});
document.addEventListener("input", (e) => {
  if (e.target.id === "decisionReason") e.target.setCustomValidity("");
});
document.addEventListener("click", (e) => {
  if (e.target.closest("[data-remove-po]")) e.target.closest(".line").remove();
});
$("#poImportForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const button = $("[type=submit]", e.target);
  button.disabled = true;
  try {
    await api("/receipt/api/purchase-orders", {
      method: "POST",
      body: JSON.stringify({
        poNumber: $("#importNumber").value,
        supplierName: $("#importSupplier").value,
        items: $$("#poLines .line").map((l) => ({
          productId: Number($("[data-po-product]", l).value),
          orderedQty: Number($("[data-po-qty]", l).value),
        })),
      }),
    });
    $("#poImportFeedback").textContent = "Đã ghi nhận PO đã duyệt.";
    await load();
  } catch (err) {
    $("#poImportFeedback").textContent = err.message;
  } finally {
    button.disabled = false;
  }
});
document.addEventListener("submit", async (e) => {
  if (e.target.id !== "limitsForm") return;
  e.preventDefault();
  try {
    await api(`/catalog/api/products/${e.target.dataset.id}/limits`, {
      method: "PUT",
      body: JSON.stringify({
        minStock: Number($("#limitMin").value),
        maxStock: Number($("#limitMax").value),
      }),
    });
    $("#detailDialog").close();
    await load();
    feedback("Đã cập nhật định mức.");
  } catch (err) {
    $("#limitsError").textContent = err.message;
  }
});
