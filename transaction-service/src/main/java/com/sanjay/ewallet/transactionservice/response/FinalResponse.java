package com.sanjay.ewallet.transactionservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FinalResponse {

    int code;
    String message;

    @NonNull
    private Object data;

    public FinalResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
