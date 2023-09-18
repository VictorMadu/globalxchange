package com.victorebubemadu.klasha.globalxchange.exception.errorcodes;

import com.victorebubemadu.klasha.globalxchange.exception.ErrorCode;

public class InfraErrorCodes {
    public static final ErrorCode INVALID_COUNTRY_NAME = new ErrorCode("INFRA_ERROR", "INVALID_COUNTRY_NAME");
    public static final ErrorCode INVALID_COUNTRY_OR_STATE_NAME = new ErrorCode("INFRA_ERROR",
            "INVALID_COUNTRY_OR_STATE_NAME");
    public static final ErrorCode FAILED_OPERATION = new ErrorCode("INFRA_ERROR", "FAILED_OPERATION");
}
