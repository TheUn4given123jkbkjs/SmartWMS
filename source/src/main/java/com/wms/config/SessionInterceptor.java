package com.wms.config;

import com.wms.dto.auth.UserResponseDTO;
import com.wms.util.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/** Session identity and catalog role guard for the workspace. Warehouse roles are enforced by services. */
public class SessionInterceptor implements HandlerInterceptor {

  private final boolean writeOnly;

  public SessionInterceptor() {
    this(false);
  }

  public SessionInterceptor(boolean writeOnly) {
    this.writeOnly = writeOnly;
  }

  @Override
  public boolean preHandle(
    HttpServletRequest request,
    HttpServletResponse response,
    Object handler
  ) throws Exception {
    boolean write =
      !"GET".equals(request.getMethod()) && !"HEAD".equals(request.getMethod());
    if (writeOnly && !write) return true;

    var session = request.getSession(false);
    var user = session == null
      ? null
      : (UserResponseDTO) session.getAttribute("user");
    String path = request.getRequestURI();
    if (user == null) {
      if (path.contains("/api/")) {
        reject(
          response,
          401,
          "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại."
        );
      } else {
        response.sendRedirect("/auth/login");
      }
      return false;
    }
    if (
      write &&
      path.startsWith("/catalog/") &&
      user.getRole() != UserRole.ROLE_MANAGER &&
      user.getRole() != UserRole.ROLE_ADMIN
    ) {
      reject(
        response,
        403,
        "Chỉ Quản lý kho và Quản trị viên được tạo danh mục."
      );
      return false;
    }
    return true;
  }

  private void reject(HttpServletResponse response, int status, String message)
    throws java.io.IOException {
    response.setStatus(status);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write("{\"message\":\"" + message + "\"}");
  }
}
