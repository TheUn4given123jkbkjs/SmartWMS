package com.wms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ngoại lệ xung đột dữ liệu (HTTP 409 Conflict).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends BusinessException {

  public ConflictException(String message) {
    super("CONFLICT", message);
  }

  public ConflictException(String errorCode, String message) {
    super(errorCode, message);
  }
}
