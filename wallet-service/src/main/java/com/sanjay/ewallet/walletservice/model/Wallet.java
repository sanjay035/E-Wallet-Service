package com.sanjay.ewallet.walletservice.model;

import com.sanjay.ewallet.walletservice.response.WalletResponse;
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
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String userId;
    private int balance;
    private String walletCreationTime;

    public WalletResponse toWalletResponse(){
        WalletResponse walletResponse = WalletResponse.builder()
                .userId(this.userId)
                .balance(this.balance)
                .build();
        return walletResponse;
    }
}
