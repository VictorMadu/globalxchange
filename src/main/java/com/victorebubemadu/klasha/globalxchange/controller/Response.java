package com.victorebubemadu.klasha.globalxchange.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.victorebubemadu.klasha.globalxchange.exception.AppException;
import com.victorebubemadu.klasha.globalxchange.exception.ErrorDetails;

public class Response<T> {

    @JsonProperty()
    @Expose()
    private String message;

    @JsonProperty()
    @Expose()
    private T data;

    @JsonProperty()
    @Expose()
    private ErrorDetails error;

    public Response(
            String message,
            T data) {
        this.message = message;

        if (isError(data))
            this.error = ((AppException) data).errorDetails();
        else
            this.data = data;
    }

    private boolean isError(T data) {
        return data instanceof AppException;
    }

    public T data() {
        if (data == null)
            throw new NullPointerException();

        return data;
    }

    public ErrorDetails error() {
        if (error == null)
            throw new NullPointerException();

        return error;
    }

}
