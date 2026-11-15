package com.wms.exception;

import com.wms.util.AppConstants;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(AppConstants.ERR_NOT_FOUND, message);
    }
}

