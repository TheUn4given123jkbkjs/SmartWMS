package com.wms.exception;

import com.wms.util.AppConstants;

public class InvalidLocationException extends BusinessException {
    public InvalidLocationException(String message) {
        super(AppConstants.ERR_BR_INB_03, message);
    }
}

