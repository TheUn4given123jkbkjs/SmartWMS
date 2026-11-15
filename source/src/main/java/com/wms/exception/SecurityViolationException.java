package com.wms.exception;

import com.wms.util.AppConstants;

public class SecurityViolationException extends BusinessException {
    public SecurityViolationException(String message) {
        super(AppConstants.ERR_BR_SEC_03, message);
    }
}

