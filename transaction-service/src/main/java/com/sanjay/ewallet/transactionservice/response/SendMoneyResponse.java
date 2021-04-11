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
public class SendMoneyResponse {
    private String transactionId;
    private String sender;
    private String receiver;
    private String purpose;
    private String status;
    private int amount;
    private String transactionTime;
}
