package com.wms.service;

import com.wms.dto.auth.LoginRequestDTO;
import com.wms.dto.auth.UserResponseDTO;

public interface AuthService {
    UserResponseDTO login(LoginRequestDTO request);
    UserResponseDTO getCurrentUser(Long userId);
}

