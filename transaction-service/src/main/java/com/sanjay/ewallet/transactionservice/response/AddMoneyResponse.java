package com.sanjay.ewallet.transactionservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AddMoneyResponse {
    private String transactionId;
    private String receiver;
    private String status;
    private int amount;
}
