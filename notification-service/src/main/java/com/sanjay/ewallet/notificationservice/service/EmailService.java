package com.sanjay.ewallet.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjay.ewallet.notificationservice.constants.EmailConstants;
import com.sanjay.ewallet.notificationservice.constants.KafkaConstants;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${simple.mail.username}")
    private String username;
    @Value("${simple.mail.subject}")
    private String subject;

    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = {KafkaConstants.KAFKA_PRODUCER_SEND_EMAIL_TOPIC},
            groupId = KafkaConstants.KAFKA_PRODUCER_SEND_EMAIL_GROUP_ID)
    public void sendMail(final String event) throws JsonProcessingException {

        JSONObject sendMailRequest = objectMapper.readValue(event, JSONObject.class);
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setTo((String) sendMailRequest.get(EmailConstants.EMAIL_TO_USER));
        simpleMailMessage.setText((String) sendMailRequest.get(EmailConstants.EMAIL_MESSAGE));

        javaMailSender.send(simpleMailMessage);
    }
}
