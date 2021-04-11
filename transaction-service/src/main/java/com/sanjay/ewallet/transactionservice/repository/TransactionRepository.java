package com.sanjay.ewallet.transactionservice.repository;

import com.sanjay.ewallet.transactionservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Modifying
    @Query("update Transaction t set t.status = :status where t.transactionId = :transactionId")
    void updateTransaction(String transactionId, String status);

    Transaction findByTransactionId(String transactionId);
}
