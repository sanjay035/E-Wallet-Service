package com.sanjay.ewallet.walletservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjay.ewallet.walletservice.constants.EmailConstants;
import com.sanjay.ewallet.walletservice.constants.KafkaConstants;
import com.sanjay.ewallet.walletservice.constants.TxnConstants;
import com.sanjay.ewallet.walletservice.constants.WalletConstants;
import com.sanjay.ewallet.walletservice.model.Wallet;
import com.sanjay.ewallet.walletservice.repository.WalletRepository;
import com.sanjay.ewallet.walletservice.util.LoggerWrapper;
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

@Service
public class WalletService {

    private static final Logger LOG = LoggerWrapper.getLogger(WalletService.class);

    @Value("${wallet.amount.default}")
    private int defaultAmount;
    @Value("${uri.user.getDetails}")
    private String getUserDetailsUri;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = {KafkaConstants.KAFKA_CONSUMER_CREATE_WALLET_TOPIC},
            groupId = KafkaConstants.KAFKA_CONSUMER_CREATE_WALLET_GROUP_ID)
    public void createWallet(final String event) throws JsonProcessingException {

        JSONObject createWalletRequest = objectMapper.readValue(event, JSONObject.class);

        Wallet wallet = Wallet.builder()
                .userId((String) createWalletRequest.get(WalletConstants.USER_ID))
                .balance(defaultAmount)
                .walletCreationTime(new Date().toString())
                .build();
        walletRepository.save(wallet);

        publishSendEmail(wallet.getUserId(),
                String.format("Hey %s, your E-Wallet has been created successfully " +
                                "with default amount of Rs.%d/-",
                        wallet.getUserId(), wallet.getBalance()));
    }

    @KafkaListener(topics = {KafkaConstants.KAFKA_CONSUMER_ADD_MONEY_TOPIC},
            groupId = KafkaConstants.KAFKA_CONSUMER_ADD_MONEY_GROUP_ID)
    public void addMoney(final String event) throws JsonProcessingException {

        JSONObject updateWalletRequest = objectMapper.readValue(event, JSONObject.class);

        String receiver = (String) updateWalletRequest.get(TxnConstants.RECEIVER);
        String transactionId = (String) updateWalletRequest.get(TxnConstants.TRANSACTION_ID);
        int amount = (int) updateWalletRequest.get(TxnConstants.AMOUNT);

        incrementWallet(receiver, amount);
        publishUpdateTransaction(KafkaConstants.KAFKA_PRODUCER_ADD_MONEY_TRANSACTION_TOPIC, transactionId, amount, TxnConstants.SUCCESSFUL);
    }

    @KafkaListener(topics = {KafkaConstants.KAFKA_CONSUMER_SEND_MONEY_TOPIC},
            groupId = KafkaConstants.KAFKA_CONSUMER_SEND_MONEY_GROUP_ID)
    public void sendMoney(final String event) throws JsonProcessingException {

        JSONObject updateWalletRequest = objectMapper.readValue(event, JSONObject.class);

        String sender = (String) updateWalletRequest.get(TxnConstants.SENDER);
        String receiver = (String) updateWalletRequest.get(TxnConstants.RECEIVER);
        String transactionId = (String) updateWalletRequest.get(TxnConstants.TRANSACTION_ID);
        int amount = (int) updateWalletRequest.get(TxnConstants.AMOUNT);

        Wallet senderWallet = walletRepository.findByUserId(sender);

        if (senderWallet.getBalance() < amount) {
            publishUpdateTransaction(KafkaConstants.KAFKA_PRODUCER_SEND_MONEY_TRANSACTION_TOPIC, transactionId, amount, TxnConstants.FAILED);
            return;
        }

        decrementWallet(sender, amount);
        incrementWallet(receiver, amount);
        publishUpdateTransaction(KafkaConstants.KAFKA_PRODUCER_SEND_MONEY_TRANSACTION_TOPIC, transactionId, amount, TxnConstants.SUCCESSFUL);
    }

    public void incrementWallet(final String userId, final int amount) {
        walletRepository.updateWalletAmount(userId, amount);
    }

    public void decrementWallet(final String userId, final int amount) {
        walletRepository.updateWalletAmount(userId, -amount);
    }

    public void publishUpdateTransaction(final String topic, final String transactionId, final int amount, final String status) throws JsonProcessingException {
        JSONObject transactionUpdate = new JSONObject();
        transactionUpdate.put(TxnConstants.TRANSACTION_ID, transactionId);
        transactionUpdate.put(TxnConstants.STATUS, status);
        transactionUpdate.put(TxnConstants.AMOUNT, amount);
        kafkaTemplate.send(topic, objectMapper.writeValueAsString(transactionUpdate));
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
