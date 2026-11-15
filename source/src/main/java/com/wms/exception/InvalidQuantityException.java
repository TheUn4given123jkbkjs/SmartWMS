package com.wms.exception;

import com.wms.util.AppConstants;

public class InvalidQuantityException extends BusinessException {
    public InvalidQuantityException(String message) {
        super(AppConstants.ERR_BR_INB_01, message);
    }
}

