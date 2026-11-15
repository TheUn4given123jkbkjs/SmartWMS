package com.wms.exception;

import com.wms.util.AppConstants;

public class ExpiredLotException extends BusinessException {
    public ExpiredLotException(String message) {
        super(AppConstants.ERR_BR_LOT_01, message);
    }
}

