package com.wms;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WorkspaceIntegrationTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper json;

  MockHttpSession login(String username) throws Exception {
    return (MockHttpSession) mvc
      .perform(
        post("/auth/api/login")
          .contentType("application/json")
          .content(
            json.writeValueAsString(
              java.util.Map.of("username", username, "password", "123456")
            )
          )
      )
      .andExpect(status().isOk())
      .andReturn()
      .getRequest()
      .getSession();
  }

  @Test
  @org.junit.jupiter.api.DisplayName("TC-AUTH.01-P: Đăng nhập thành công với vai trò Nhân viên kho (Maker)")
  void tc_auth_01_p_staffLoginSuccess() throws Exception {
    var session = login("staff_thien");
    mvc
      .perform(get("/app").session(session))
      .andExpect(status().isOk())
      .andExpect(content().string(org.hamcrest.Matchers.containsString("Tôn Thanh Thiên")));
  }

  @Test
  @org.junit.jupiter.api.DisplayName("TC-AUTH.02-P: Đăng nhập thành công với vai trò Quản lý kho (Checker)")
  void tc_auth_02_p_managerLoginSuccess() throws Exception {
    var session = login("manager_khang");
    mvc
      .perform(get("/app").session(session))
      .andExpect(status().isOk())
      .andExpect(content().string(org.hamcrest.Matchers.containsString("Nguyễn Bảo Khang")));
  }

  @Test
  @org.junit.jupiter.api.DisplayName("TC-AUTH.03-P: Đăng nhập thành công với vai trò Quản trị viên (Admin)")
  void tc_auth_03_p_adminLoginSuccess() throws Exception {
    var session = login("admin");
    mvc
      .perform(get("/app").session(session))
      .andExpect(status().isOk())
      .andExpect(content().string(org.hamcrest.Matchers.containsString("Quản trị viên Hệ thống")));
  }

  @Test
  @org.junit.jupiter.api.DisplayName("TC-AUTH.01-N: Chặn đăng nhập khi mật khẩu không chính xác")
  void tc_auth_01_n_wrongPasswordRejected() throws Exception {
    mvc
      .perform(
        post("/auth/api/login")
          .contentType("application/json")
          .content(
            json.writeValueAsString(
              java.util.Map.of("username", "staff_thien", "password", "WrongPassword_999")
            )
          )
      )
      .andExpect(status().isForbidden())
      .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Mật khẩu không chính xác")));
  }

  @Test
  @org.junit.jupiter.api.DisplayName("TC-AUTH.03-N: Chặn đăng nhập khi tên tài khoản không tồn tại trong hệ thống")
  void tc_auth_03_n_nonExistentUserRejected() throws Exception {
    mvc
      .perform(
        post("/auth/api/login")
          .contentType("application/json")
          .content(
            json.writeValueAsString(
              java.util.Map.of("username", "user_khong_ton_tai", "password", "123456")
            )
          )
      )
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("không tồn tại")));
  }

  @Test
  @org.junit.jupiter.api.DisplayName("TC-AUTH.04-N: Chặn truy cập trái phép khi chưa đăng nhập (Session Guard)")
  void tc_auth_04_n_unauthenticatedAccessRedirected() throws Exception {
    mvc.perform(get("/receipt/create")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/auth/login"));
    mvc.perform(get("/inventory/stock-card")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/auth/login"));
  }

  @Test
  @org.junit.jupiter.api.DisplayName("TC-AUTH: Trang Landing chứa Modal đăng nhập và Script nạp API")
  void tc_auth_landingPageContainsLoginModalAndScript() throws Exception {
    String html = mvc
      .perform(get("/"))
      .andExpect(status().isOk())
      .andReturn()
      .getResponse()
      .getContentAsString();
    assertTrue(html.contains("modalLoginForm"), "Landing page must contain modalLoginForm");
    assertTrue(html.contains("/auth/api/login"), "Landing page must include the login script invoking /auth/api/login");
  }

  @Test
  void pagesRenderWithSharedLayoutAndAuthenticatedIdentity() throws Exception {
    var session = login("manager_trung");
    for (String route : new String[] {
      "/app",
      "/catalog/products",
      "/catalog/locations",
      "/receipt/create",
      "/receipt/approve",
      "/issue/create",
      "/issue/pick",
      "/inventory/stock-card",
      "/inventory/alerts",
    }) {
      String html = mvc
        .perform(get(route).session(session))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
      assertTrue(html.contains("aria-current=\"page\""), route);
      assertTrue(html.contains("/js/workspace.js"), route);
      assertFalse(html.contains("blank-canvas-card"), route);
    }
  }

  @Test
  void sessionAndCatalogRolesAreEnforced() throws Exception {
    mvc.perform(get("/app")).andExpect(status().is3xxRedirection());
    mvc
      .perform(get("/catalog/api/products"))
      .andExpect(status().isUnauthorized());
    var staff = login("staff_thien");
    mvc
      .perform(
        post("/catalog/api/products")
          .session(staff)
          .contentType("application/json")
          .content("{}")
      )
      .andExpect(status().isForbidden());
    mvc
      .perform(post("/auth/logout").session(staff))
      .andExpect(redirectedUrl("/auth/login"));
    assertTrue(staff.isInvalid());
  }

  @Test
  void catalogWritesPersistAndDuplicateIsReported() throws Exception {
    var session = login("admin");
    long category = json
      .readTree(
        mvc
          .perform(get("/catalog/api/categories").session(session))
          .andReturn()
          .getResponse()
          .getContentAsString()
      )
      .get(0)
      .get("id")
      .asLong();
    String payload =
      "{\"skuCode\":\"UI-TEST-001\",\"name\":\"UI test\",\"uom\":\"Cái\",\"categoryId\":" +
      category +
      ",\"minStock\":1,\"maxStock\":10,\"manageByLot\":true}";
    mvc
      .perform(
        post("/catalog/api/products")
          .session(session)
          .contentType("application/json")
          .content(payload)
      )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.skuCode").value("UI-TEST-001"));
    mvc
      .perform(
        post("/catalog/api/products")
          .session(session)
          .contentType("application/json")
          .content(payload)
      )
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.message").exists());
    mvc
      .perform(
        post("/catalog/api/locations")
          .session(session)
          .contentType("application/json")
          .content(
            "{\"locationCode\":\"RAW-UI-TEST\",\"zone\":\"RAW\",\"maxCapacity\":100,\"shelf\":\"01\"}"
          )
      )
      .andExpect(status().isCreated());
    mvc
      .perform(get("/catalog/api/locations").session(session))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").exists());
  }

  @Test
  void warehouseWritesRequireStaffOrManagerRole() throws Exception {
    var session = login("admin");
    mvc
      .perform(post("/issue/api/notes/1/approve").session(session))
      .andExpect(status().isForbidden());
  }
}
