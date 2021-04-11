package com.sanjay.ewallet.transactionservice.model;

import com.sanjay.ewallet.transactionservice.response.AddMoneyResponse;
import com.sanjay.ewallet.transactionservice.response.SendMoneyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    private String transactionId;
    private String sender;
    private String receiver;
    private String purpose;
    private String status;
    private int amount;
    private String transactionTime;

    public SendMoneyResponse toSendMoneyResponse(){
        SendMoneyResponse sendMoneyResponse = SendMoneyResponse.builder()
                .transactionId(this.transactionId)
                .sender(this.sender)
                .receiver(this.receiver)
                .purpose(this.purpose)
                .status(this.status)
                .amount(this.amount)
                .transactionTime(this.transactionTime)
                .build();
        return sendMoneyResponse;
    }

    public AddMoneyResponse toAddMoneyResponse(){
        AddMoneyResponse addMoneyResponse = AddMoneyResponse.builder()
                .transactionId(this.transactionId)
                .receiver(this.receiver)
                .status(this.status)
                .amount(this.amount)
                .build();
        return addMoneyResponse;
    }
}
