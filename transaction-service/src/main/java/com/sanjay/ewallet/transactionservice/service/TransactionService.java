package com.sanjay.ewallet.transactionservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjay.ewallet.transactionservice.constants.EmailConstants;
import com.sanjay.ewallet.transactionservice.constants.KafkaConstants;
import com.sanjay.ewallet.transactionservice.constants.TxnConstants;
import com.sanjay.ewallet.transactionservice.model.Transaction;
import com.sanjay.ewallet.transactionservice.model.TransactionStatus;
import com.sanjay.ewallet.transactionservice.repository.TransactionRepository;
import com.sanjay.ewallet.transactionservice.request.AddMoneyRequest;
import com.sanjay.ewallet.transactionservice.request.SendMoneyRequest;
import com.sanjay.ewallet.transactionservice.response.AddMoneyResponse;
import com.sanjay.ewallet.transactionservice.response.SendMoneyResponse;
import com.sanjay.ewallet.transactionservice.util.LoggerWrapper;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TransactionService {

    private static final Logger LOG = LoggerWrapper.getLogger(TransactionService.class);

    @Value("${uri.user.getDetails}")
    private String getUserDetailsUri;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public AddMoneyResponse addMoney(final AddMoneyRequest addMoneyRequest) throws JsonProcessingException {
        Transaction transaction = Transaction.builder()
                .sender("E-Wallet")
                .receiver(addMoneyRequest.getReceiver())
                .amount(addMoneyRequest.getAmount())
                .purpose("Add money to the wallet")
                .transactionId(UUID.randomUUID().toString())
                .transactionTime(new Date().toString())
                .status(TransactionStatus.PENDING.toString())
                .build();
        transactionRepository.save(transaction);

        JSONObject walletRequest = new JSONObject();
        walletRequest.put(TxnConstants.RECEIVER, transaction.getReceiver());
        walletRequest.put(TxnConstants.AMOUNT, transaction.getAmount());
        walletRequest.put(TxnConstants.TRANSACTION_ID, transaction.getTransactionId());

        kafkaTemplate.send(KafkaConstants.KAFKA_PRODUCER_ADD_MONEY_TOPIC, 
                objectMapper.writeValueAsString(walletRequest));

        return transaction.toAddMoneyResponse();
    }

    public SendMoneyResponse sendMoney(final SendMoneyRequest sendMoneyRequest) throws JsonProcessingException {
        Transaction transaction = Transaction.builder()
                .sender(sendMoneyRequest.getSender())
                .receiver(sendMoneyRequest.getReceiver())
                .amount(sendMoneyRequest.getAmount())
                .purpose(sendMoneyRequest.getPurpose())
                .transactionId(UUID.randomUUID().toString())
                .transactionTime(new Date().toString())
                .status(TransactionStatus.PENDING.toString())
                .build();
        transactionRepository.save(transaction);

        JSONObject walletRequest = new JSONObject();
        walletRequest.put(TxnConstants.SENDER, transaction.getSender());
        walletRequest.put(TxnConstants.RECEIVER, transaction.getReceiver());
        walletRequest.put(TxnConstants.AMOUNT, transaction.getAmount());
        walletRequest.put(TxnConstants.TRANSACTION_ID, transaction.getTransactionId());

        kafkaTemplate.send(KafkaConstants.KAFKA_PRODUCER_SEND_MONEY_TOPIC, 
                objectMapper.writeValueAsString(walletRequest));

        return transaction.toSendMoneyResponse();
    }

    @KafkaListener(topics = {KafkaConstants.KAFKA_CONSUMER_ADD_MONEY_TRANSACTION_TOPIC},
            groupId = KafkaConstants.KAFKA_CONSUMER_ADD_MONEY_TRANSACTION_GROUP_ID)
    public void addMoneyUpdate(final String event) throws JsonProcessingException {

        JSONObject updateTransactionRequest = objectMapper.readValue(event, JSONObject.class);

        String transactionId = (String) updateTransactionRequest.get(TxnConstants.TRANSACTION_ID);
        String status = (String) updateTransactionRequest.get(TxnConstants.STATUS);
        int amount = (int) updateTransactionRequest.get(TxnConstants.AMOUNT);

        transactionRepository.updateTransaction(transactionId, status);

        Transaction transaction = transactionRepository.findByTransactionId(transactionId);

        String receiver = transaction.getReceiver();

        publishSendEmail(receiver,
                String.format("Hey %s, adding of Rs.%d/- to your wallet with transaction Id %s is %s",
                        receiver, amount, transactionId, status));
    }
    
    @KafkaListener(topics = {KafkaConstants.KAFKA_CONSUMER_SEND_MONEY_TRANSACTION_TOPIC},
            groupId = KafkaConstants.KAFKA_CONSUMER_SEND_MONEY_TRANSACTION_GROUP_ID)
    public void sendMoneyUpdate(final String event) throws JsonProcessingException {

        JSONObject updateTransactionRequest = objectMapper.readValue(event, JSONObject.class);

        String transactionId = (String) updateTransactionRequest.get(TxnConstants.TRANSACTION_ID);
        String status = (String) updateTransactionRequest.get(TxnConstants.STATUS);
        int amount = (int) updateTransactionRequest.get(TxnConstants.AMOUNT);

        transactionRepository.updateTransaction(transactionId, status);

        Transaction transaction = transactionRepository.findByTransactionId(transactionId);

        String sender = transaction.getSender();
        String receiver = transaction.getReceiver();

        publishSendEmail(sender,
                String.format("Hey %s, sending of Rs.%d/- to %s with transaction Id %s is %s",
                sender, amount, receiver, transactionId, status));

        if(status.equals(TransactionStatus.SUCCESSFUL.toString())){
           publishSendEmail(receiver,
                   String.format("Hey %s, you have received Rs.%d/- from %s",
                   receiver, amount, sender));
        }
    }

    public void publishSendEmail(final String userId, final String mailMessage) throws JsonProcessingException {
        URI uri = URI.create(getUserDetailsUri + userId);
        HttpEntity httpEntity = new HttpEntity(new HttpHeaders());
        JSONObject jsonObject = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, JSONObject.class).getBody();

        Map userData = (LinkedHashMap) jsonObject.get("data");

        String userEmailId = (String) userData.get(EmailConstants.EMAIL_ID);

        JSONObject sendEmailRequest = new JSONObject();
        sendEmailRequest.put(EmailConstants.EMAIL_TO_USER, userEmailId);
        sendEmailRequest.put(EmailConstants.EMAIL_MESSAGE, mailMessage);

        kafkaTemplate.send(KafkaConstants.KAFKA_PRODUCER_SEND_EMAIL_TOPIC, 
                objectMapper.writeValueAsString(sendEmailRequest));
    }
}
