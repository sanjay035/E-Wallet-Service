package com.sanjay.ewallet.walletservice.constants;

public class KafkaConstants {
    public static final String KAFKA_PRODUCER_ADD_MONEY_TRANSACTION_TOPIC = "addMoneyUpdate";
    public static final String KAFKA_PRODUCER_SEND_MONEY_TRANSACTION_TOPIC = "sendMoneyUpdate";

    public static final String KAFKA_CONSUMER_CREATE_WALLET_TOPIC = "createWallet";
    public static final String KAFKA_CONSUMER_CREATE_WALLET_GROUP_ID = "createWalletConsumers";

    public static final String KAFKA_CONSUMER_ADD_MONEY_TOPIC = "addMoney";
    public static final String KAFKA_CONSUMER_ADD_MONEY_GROUP_ID = "addMoneyConsumers";

    public static final String KAFKA_CONSUMER_SEND_MONEY_TOPIC = "sendMoney";
    public static final String KAFKA_CONSUMER_SEND_MONEY_GROUP_ID = "sendMoneyConsumers";

    public static final String KAFKA_PRODUCER_SEND_EMAIL_TOPIC = "sendEmail";
}
