package com.sanjay.ewallet.transactionservice.request;

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
public class SendMoneyRequest {
    private String sender;
    private String receiver;
    private int amount;
    private String purpose;
}
