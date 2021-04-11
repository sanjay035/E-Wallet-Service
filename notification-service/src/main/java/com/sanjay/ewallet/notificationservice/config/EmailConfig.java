package com.sanjay.ewallet.notificationservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjay.ewallet.notificationservice.constants.EmailConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${simple.mail.host}")
    private String host;
    @Value("${simple.mail.port}")
    private int port;
    @Value("${simple.mail.username}")
    private String username;
    @Value("${simple.mail.password}")
    private String password;
    @Value("${mail.smtp.starttls.enable}")
    private String mailSMTPStartTLSEnable;
    @Value("${mail.debug}")
    private String mailDebug;

    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put(EmailConstants.MAIL_SMTP_START_TLS_ENABLE, mailSMTPStartTLSEnable);
        properties.put(EmailConstants.MAIL_DEBUG, mailDebug);

        return javaMailSender;
    }

    @Bean
    public SimpleMailMessage simpleMailMessage(){
        return new SimpleMailMessage();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
