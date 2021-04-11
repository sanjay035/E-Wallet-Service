package com.sanjay.ewallet.transactionservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sanjay.ewallet.transactionservice.request.AddMoneyRequest;
import com.sanjay.ewallet.transactionservice.request.SendMoneyRequest;
import com.sanjay.ewallet.transactionservice.service.TransactionService;
import com.sanjay.ewallet.transactionservice.util.ResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PutMapping("/send-money")
    public ResponseEntity<Object> sendMoney(@RequestBody SendMoneyRequest sendMoneyRequest) throws JsonProcessingException {
        return new ResponseEntity<>(ResponseGenerator.okResponse(transactionService.sendMoney(sendMoneyRequest)), HttpStatus.OK);
    }

    @PutMapping("/add-money")
    public ResponseEntity<Object> addMoney(@RequestBody AddMoneyRequest addMoneyRequest) throws JsonProcessingException {
        return new ResponseEntity<>(ResponseGenerator.okResponse(transactionService.addMoney(addMoneyRequest)), HttpStatus.OK);
    }
}
