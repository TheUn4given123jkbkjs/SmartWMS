package com.wms;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthFlowTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper json;

  @Test
  void testLandingPageContainsLoginScript() throws Exception {
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
  void testLoginWithCookieAndAccessApp() throws Exception {
    var loginResult = mvc
      .perform(
        post("/auth/api/login")
          .contentType("application/json")
          .content(
            json.writeValueAsString(
              java.util.Map.of("username", "admin", "password", "123456")
            )
          )
      )
      .andExpect(status().isOk())
      .andReturn();

    var session = loginResult.getRequest().getSession(false);
    assertNotNull(session, "Session must exist");
    assertNotNull(session.getAttribute("user"), "User in session must exist");

    // Test access /app with session
    mvc
      .perform(get("/app").session((org.springframework.mock.web.MockHttpSession) session))
      .andExpect(status().isOk());

    // Test access /auth/api/me with session
    mvc
      .perform(get("/auth/api/me").session((org.springframework.mock.web.MockHttpSession) session))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.username").value("admin"));
  }
}
