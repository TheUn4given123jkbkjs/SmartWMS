package com.wms.controller;

import com.wms.dto.auth.LoginRequestDTO;
import com.wms.dto.auth.UserResponseDTO;
import com.wms.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller Xác thực, Đăng nhập và Quản lý phiên làm việc.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Màn hình Đăng nhập riêng biệt (SCR01 /login).
     */
    @GetMapping("/login")
    public String loginView(Model model) {
        return "auth/login";
    }

    /**
     * API xử lý đăng nhập qua AJAX / Fetch.
     */
    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<UserResponseDTO> loginApi(@Valid @RequestBody LoginRequestDTO request) {
        UserResponseDTO user = authService.login(request);
        return ResponseEntity.ok(user);
    }
}
