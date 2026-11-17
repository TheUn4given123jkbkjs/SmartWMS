/* Warehouse floor plan: illustrative geometry, database-backed locations. */
(() => {
  "use strict";
  const baseWidth = 1100,
    baseHeight = 770;
  let floorWidth = baseWidth,
    floorHeight = baseHeight;
  let view = "map",
    zoom = null,
    all = [],
    matching = [];
  const zones = {
    DOCK: { name: "Tiếp nhận & giao hàng", code: "01", icon: "truck" },
    INSP: { name: "Kiểm tra hàng hóa", code: "02", icon: "clipboard-check" },
    FG: { name: "Kho thành phẩm", code: "03", icon: "boxes" },
    RAW: { name: "Kho nguyên liệu", code: "04", icon: "box-seam" },
    SCRAP: { name: "Hàng cách ly", code: "05", icon: "exclamation-triangle" },
  };
  const esc = (value) =>
    String(value ?? "—").replace(
      /[&<>"']/g,
      (c) =>
        ({
          "&": "&amp;",
          "<": "&lt;",
          ">": "&gt;",
          '"': "&quot;",
          "'": "&#39;",
        })[c],
    );
  const format = (value) =>
    value == null ? "—" : new Intl.NumberFormat("vi-VN").format(value);
  function locationButton(row, matches) {
    const occupied = row.status === "OCCUPIED";
    return `<button type="button" class="warehouse-slot ${occupied ? "slot-occupied" : "slot-empty"} ${matches ? "" : "slot-filtered"}" data-location="${esc(row.id)}" ${matches ? "" : "disabled"} aria-label="${esc(row.locationCode)}, ${occupied ? "đang chứa hàng" : "trống"}, ${esc(zones[row.zone]?.name || row.zone)}" title="${esc(row.locationCode)} · ${occupied ? "Đang chứa hàng" : "Trống"}"><span class="warehouse-pallet" aria-hidden="true"><i></i><i></i><i></i><i></i><i></i><i></i></span><strong>${esc(row.locationCode)}</strong><span class="slot-status"><i class="status-dot ${occupied ? "is-occupied" : "is-empty"}" aria-hidden="true"></i>${occupied ? "Đang chứa" : "Trống"}</span></button>`;
  }
  function zone(code, ids) {
    const config = zones[code],
      rows = all.filter((r) => r.zone === code),
      visible = rows.filter((r) => ids.has(r.id)).length;
    return `<section class="warehouse-zone warehouse-zone-${code.toLowerCase()} ${rows.length && !visible ? "zone-filtered" : ""}" aria-label="${config.name}"><header><span class="zone-number">${config.code}</span><div><span class="zone-abbr">${code}</span><h3>${config.name}</h3></div><span class="zone-count">${visible}/${rows.length} ô</span></header><div class="warehouse-racks">${rows.length ? rows.map((r) => locationButton(r, ids.has(r.id))).join("") : '<p class="zone-no-location">Chưa có vị trí được thiết lập</p>'}</div></section>`;
  }
  function plan(ids) {
    return `<div class="warehouse-viewport" tabindex="0" aria-label="Mặt bằng kho có thể cuộn"><div class="warehouse-scale"><div class="warehouse-floor">
      <div class="warehouse-site-label"><span class="site-mark">W</span><div>SMARTWMS <small>WAREHOUSE FLOOR PLAN</small></div><span class="north-mark">↑<small>BẮC</small></span></div>
      <div class="warehouse-wall top-wall"></div><div class="warehouse-wall right-wall"></div><div class="warehouse-wall bottom-wall"></div>
      <div class="warehouse-pipe" aria-hidden="true"><i></i><i></i><i></i></div><div class="fire-station"><i class="bi bi-fire" aria-hidden="true"></i> PCCC</div>
      <div class="warehouse-equipment"><i class="bi bi-tools" aria-hidden="true"></i><strong>Thiết bị & dụng cụ</strong><span>Khu phụ trợ</span><div class="equipment-shapes" aria-hidden="true"><i></i><i></i><i></i></div></div>
      <div class="warehouse-door door-in"><span>NHẬP HÀNG</span><i class="bi bi-arrow-right" aria-hidden="true"></i></div><div class="warehouse-door door-out"><span>XUẤT HÀNG</span><i class="bi bi-arrow-left" aria-hidden="true"></i></div>
      <div class="warehouse-lane lane-horizontal"><span><i class="bi bi-arrow-right" aria-hidden="true"></i> LỐI DI CHUYỂN XE NÂNG <i class="bi bi-arrow-left" aria-hidden="true"></i></span></div><div class="warehouse-lane lane-vertical"><span>LỐI VẬN CHUYỂN</span></div>
      <div class="pedestrian-crossing" aria-hidden="true"></div><div class="floor-forklift" aria-hidden="true"><i class="bi bi-truck-flatbed"></i></div>
      ${zone("DOCK", ids)}${zone("INSP", ids)}${zone("FG", ids)}${zone("RAW", ids)}${zone("SCRAP", ids)}
      <section class="warehouse-production"><span class="zone-abbr">KHU PHỤ TRỢ</span><h3><i class="bi bi-gear-wide-connected" aria-hidden="true"></i> Khu sản xuất</h3><div class="production-line" aria-hidden="true"><i></i><i></i><i></i><i></i><i></i></div><small>Minh họa khu vực vận hành</small></section>
      <section class="warehouse-offices"><div><i class="bi bi-pc-display" aria-hidden="true"></i><strong>Văn phòng kho</strong></div><div><i class="bi bi-door-open" aria-hidden="true"></i><strong>Phòng phụ trợ</strong></div><small>KHU PHỤ TRỢ · MINH HỌA</small></section>
      <div class="warehouse-dimension">MẶT BẰNG MÔ PHỎNG · QUY HOẠCH THEO PHÂN KHU 3M / 5S</div>
    </div></div></div>`;
  }
  function list() {
    return matching.length
      ? `<div class="table-wrap warehouse-list"><table class="work-table"><thead><tr><th>Mã ô kệ</th><th>Khu vực</th><th>Dãy / Kệ / Tầng / Ô</th><th>Trạng thái</th><th>Sức chứa thiết kế</th><th></th></tr></thead><tbody>${matching.map((r) => `<tr><td><strong>${esc(r.locationCode)}</strong></td><td>${esc(zones[r.zone]?.name || r.zone)}</td><td>${[r.aisle, r.rack, r.shelf, r.bin].map(esc).join(" / ")}</td><td><span class="slot-status"><i class="status-dot ${r.status === "OCCUPIED" ? "is-occupied" : "is-empty"}"></i>${r.status === "OCCUPIED" ? "Đang chứa hàng" : "Trống"}</span></td><td>${format(r.maxCapacity)}</td><td><button class="btn btn-sm btn-light" data-location="${esc(r.id)}">Chi tiết <i class="bi bi-arrow-up-right" aria-hidden="true"></i></button></td></tr>`).join("")}</tbody></table></div>`
      : '<div class="empty">Không có vị trí phù hợp với bộ lọc.</div>';
  }
  /** Size each zone from its natural content, then move neighboring regions and lanes.
   * All measurements are unscaled CSS pixels. Filters never alter the layout.
   */
  function layoutFloor() {
    const floor = document.querySelector(".warehouse-floor");
    if (!floor) return;
    const zoneConfig = {
      DOCK: {
        minColumns: 1,
        cell: 112,
        minWidth: 146,
        minHeight: 277,
        top: 142,
      },
      INSP: {
        minColumns: 2,
        cell: 100,
        minWidth: 254,
        minHeight: 210,
        top: 205,
      },
      FG: { minColumns: 3, cell: 120, minWidth: 416, minHeight: 248, top: 195 },
      RAW: { minColumns: 2, cell: 110, minWidth: 268, minHeight: 160 },
      SCRAP: { minColumns: 1, cell: 105, minWidth: 139, minHeight: 160 },
    };
    const measured = {};
    for (const [code, config] of Object.entries(zoneConfig)) {
      const element = floor.querySelector(
        ".warehouse-zone-" + code.toLowerCase(),
      );
      const count = all.filter((row) => row.zone === code).length;
      // Grow in both directions instead of creating a very tall single-column zone.
      const columns = Math.max(
        config.minColumns,
        Math.ceil(Math.sqrt(count / 2)),
      );
      const width = Math.max(
        config.minWidth,
        columns * config.cell + (columns - 1) * 9 + 40,
      );
      element.style.width = width + "px";
      element.style.minHeight = config.minHeight + "px";
      element.querySelector(".warehouse-racks").style.gridTemplateColumns =
        `repeat(${columns}, minmax(0, 1fr))`;
      measured[code] = {
        element,
        width,
        height: element.offsetHeight,
        top: config.top,
      };
    }
    const place = (element, x, y) => {
      element.style.left = x + "px";
      element.style.top = y + "px";
    };
    let topRight = 78,
      topBottom = 0;
    for (const [index, code] of ["DOCK", "INSP", "FG"].entries()) {
      const region = measured[code];
      if (index) topRight += index === 1 ? 54 : 67;
      place(region.element, topRight, region.top);
      topRight += region.width;
      topBottom = Math.max(topBottom, region.top + region.height);
    }
    const laneTop = Math.max(463, topBottom + 20);
    const bottomTop = laneTop + 87;
    let bottomRight = 78,
      bottomBottom = bottomTop;
    for (const code of ["SCRAP", "RAW"]) {
      const region = measured[code];
      place(region.element, bottomRight, bottomTop);
      bottomRight += region.width + 27;
      bottomBottom = Math.max(bottomBottom, bottomTop + region.height);
    }
    const production = floor.querySelector(".warehouse-production");
    place(production, bottomRight + 8, bottomTop);
    bottomRight += 8 + production.offsetWidth + 31;
    const offices = floor.querySelector(".warehouse-offices");
    place(offices, bottomRight, bottomTop);
    bottomRight += offices.offsetWidth;
    bottomBottom = Math.max(
      bottomBottom,
      bottomTop + production.offsetHeight,
      bottomTop + offices.offsetHeight,
    );
    floorWidth = Math.max(baseWidth, topRight + 85, bottomRight + 85);
    floorHeight = Math.max(baseHeight, bottomBottom + 60);
    floor.style.width = floorWidth + "px";
    floor.style.height = floorHeight + "px";
    floor.querySelector(".lane-horizontal").style.top = laneTop + "px";
    floor.querySelector(".lane-vertical").style.height =
      laneTop - 190 + 3 + "px";
    floor.querySelector(".pedestrian-crossing").style.top = laneTop + 2 + "px";
    place(
      floor.querySelector(".floor-forklift"),
      measured.FG.element.offsetLeft - 63,
      laneTop - 42,
    );
    floor.querySelector(".door-out").style.top = bottomTop + 50 + "px";
  }
  function updateScale() {
    const viewport = document.querySelector(".warehouse-viewport");
    if (!viewport) return;
    if (zoom === null)
      zoom = Math.min(
        1,
        Math.max(0.55, (viewport.clientWidth - 40) / floorWidth),
      );
    document.querySelector(".warehouse-floor").style.transform =
      `scale(${zoom})`;
    const wrapper = document.querySelector(".warehouse-scale");
    wrapper.style.width = `${floorWidth * zoom}px`;
    wrapper.style.height = `${floorHeight * zoom}px`;
    document.getElementById("warehouseZoom").textContent =
      `${Math.round(zoom * 100)}%`;
  }
  function render(records, rows) {
    all = records;
    matching = rows;
    const ids = new Set(rows.map((r) => r.id));
    const occupied = all.filter((r) => r.status === "OCCUPIED").length;
    document.getElementById("warehouseOverview").innerHTML = [
      ["grid-3x3", "Tổng vị trí", all.length, "Trong 5 phân khu", "total"],
      [
        "box-seam",
        "Đang chứa hàng",
        occupied,
        "Vị trí có tồn thực tế",
        "occupied",
      ],
      [
        "bounding-box",
        "Vị trí trống",
        all.length - occupied,
        "Sẵn sàng tiếp nhận hàng",
        "empty",
      ],
      [
        "funnel",
        "Kết quả đang xem",
        matching.length,
        "Theo điều kiện tìm kiếm",
        "result",
      ],
    ]
      .map(
        ([icon, label, value, note, type]) =>
          `<div class="warehouse-stat stat-${type}"><span class="warehouse-stat-icon"><i class="bi bi-${icon}" aria-hidden="true"></i></span><div><span>${label}</span><strong>${format(value)}<small> vị trí</small></strong><p>${note}</p></div></div>`,
      )
      .join("");
    document.getElementById("warehouseResult").textContent =
      `Hiển thị ${matching.length} / ${all.length} vị trí` +
      (matching.length ? "" : " · Không có kết quả phù hợp");
    const host = document.getElementById("locations");
    const oldScroll = host.querySelector(".warehouse-viewport");
    const position = oldScroll
      ? { left: oldScroll.scrollLeft, top: oldScroll.scrollTop }
      : null;
    host.innerHTML = view === "map" ? plan(ids) : list();
    layoutFloor();
    updateScale();
    if (position) {
      const viewport = host.querySelector(".warehouse-viewport");
      if (viewport) {
        viewport.scrollLeft = position.left;
        viewport.scrollTop = position.top;
      }
    }
    document
      .querySelectorAll("[data-map-view]")
      .forEach((b) =>
        b.setAttribute("aria-pressed", String(b.dataset.mapView === view)),
      );
    document.querySelector(".warehouse-zoom").hidden = view !== "map";
  }
  document.addEventListener("click", (e) => {
    const button = e.target.closest("button");
    if (!button) return;
    if (button.dataset.mapView) {
      view = button.dataset.mapView;
      render(all, matching);
    }
    if (button.dataset.mapZoom) {
      const viewport = document.querySelector(".warehouse-viewport");
      if (!viewport) return;
      const action = button.dataset.mapZoom;
      zoom =
        action === "fit"
          ? Math.min(1.4, (viewport.clientWidth - 40) / floorWidth)
          : Math.max(
              0.15,
              Math.min(1.6, (zoom || 1) + (action === "in" ? 0.15 : -0.15)),
            );
      updateScale();
    }
  });
  // Web fonts can change header/slot height after the initial API response.
  document.fonts?.ready.then(() => {
    layoutFloor();
    updateScale();
  });
  window.WarehouseMap = { render };
})();
