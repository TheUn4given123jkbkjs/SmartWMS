package com.wms.exception;

import com.wms.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ngoại lệ lô hàng đã hết hạn sử dụng (HTTP 409 Conflict).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ExpiredLotException extends BusinessException {
    public ExpiredLotException(String message) {
        super(AppConstants.ERR_BR_LOT_01, message);
    }
}


