package com.victorebubemadu.klasha.globalxchange.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

public final class AppException extends RuntimeException {
    @JsonProperty()
    @Expose()
    private String code;

    @JsonProperty()
    @Expose()

    private String message;

    @JsonProperty()
    @Expose()

    private String extraDetails;

    public AppException(ErrorCode errorCode) {
        this(errorCode, null);

    }

    public AppException(ErrorCode errorCode,
            String extraDetails) {

        this.code = errorCode.code();
        this.message = errorCode.message();
        this.extraDetails = extraDetails;

    }

    public AppException(String code, String message) {

        this(code, message, null);

    }

    public AppException(String code, String message,
            String extraDetails) {

        this.code = code;
        this.message = message;
        this.extraDetails = extraDetails;

    }

    public ErrorDetails errorDetails() {
        return new ErrorDetails(code, message, extraDetails);
    }

}
