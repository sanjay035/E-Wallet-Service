package com.sanjay.ewallet.transactionservice.constants;

public class KafkaConstants {
    public static final String KAFKA_PRODUCER_ADD_MONEY_TOPIC = "addMoney";
    public static final String KAFKA_PRODUCER_SEND_MONEY_TOPIC = "sendMoney";
    public static final String KAFKA_PRODUCER_SEND_EMAIL_TOPIC = "sendEmail";

    public static final String KAFKA_CONSUMER_ADD_MONEY_TRANSACTION_TOPIC = "addMoneyUpdate";
    public static final String KAFKA_CONSUMER_ADD_MONEY_TRANSACTION_GROUP_ID = "addMoneyUpdateConsumers";

    public static final String KAFKA_CONSUMER_SEND_MONEY_TRANSACTION_TOPIC = "sendMoneyUpdate";
    public static final String KAFKA_CONSUMER_SEND_MONEY_TRANSACTION_GROUP_ID = "sendMoneyUpdateConsumers";
}
