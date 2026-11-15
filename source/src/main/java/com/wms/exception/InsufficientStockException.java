package com.wms.exception;

import com.wms.util.AppConstants;

public class InsufficientStockException extends BusinessException {
    public InsufficientStockException(String message) {
        super(AppConstants.ERR_BR_OUT_01, message);
    }
}

