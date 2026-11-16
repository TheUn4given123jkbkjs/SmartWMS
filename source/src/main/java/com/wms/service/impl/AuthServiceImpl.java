package com.wms.service.impl;

import com.wms.dto.auth.LoginRequestDTO;
import com.wms.dto.auth.UserResponseDTO;
import com.wms.entity.User;
import com.wms.exception.ResourceNotFoundException;
import com.wms.exception.SecurityViolationException;
import com.wms.repository.UserRepository;
import com.wms.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hiện thực Service xác thực người dùng và phân quyền (RBAC).
 */
@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("Tài khoản không tồn tại: " + request.getUsername()));

        if (user.getActive() != null && !user.getActive()) {
            throw new SecurityViolationException("Tài khoản đang bị khóa.");
        }

        // Kiểm tra mật khẩu (hỗ trợ hash / demo plaintext)
        if (!user.getPasswordHash().equals(request.getPassword())) {
            throw new SecurityViolationException("Mật khẩu không chính xác.");
        }

        return new UserResponseDTO(user.getId(), user.getUsername(), user.getFullName(), user.getRole());
    }

    @Override
    public UserResponseDTO getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng có ID: " + userId));

        return new UserResponseDTO(user.getId(), user.getUsername(), user.getFullName(), user.getRole());
    }
}
