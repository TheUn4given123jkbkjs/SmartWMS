package com.wms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ngoại lệ xung đột dữ liệu (HTTP 409 Conflict).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends BusinessException {

  public ConflictException(String message) {
    super("ERR_CONFLICT", message);
  }
}
