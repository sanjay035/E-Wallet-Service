package com.sanjay.ewallet.transactionservice.exception;

import com.sanjay.ewallet.transactionservice.constants.ErrorCodes;
import com.sanjay.ewallet.transactionservice.response.FinalResponse;

public class FinalException extends Exception{

    private static final long serialVersionUID = 1L;

    private final FinalResponse errorResponse;

    public FinalException(int code, String message) {
        super(message);
        errorResponse = new FinalResponse();
        errorResponse.setCode(code);
        errorResponse.setMessage(message);
    }

    @Override
    public String getMessage() {
        if (errorResponse != null) {
            return errorResponse.getMessage();
        }
        return super.getMessage();
    }

    public int getCode() {
        if (errorResponse != null) {
            return errorResponse.getCode();
        }
        return ErrorCodes.GENERIC_ERROR;
    }
}
