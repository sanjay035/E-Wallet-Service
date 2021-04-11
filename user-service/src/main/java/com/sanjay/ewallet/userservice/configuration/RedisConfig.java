package com.sanjay.ewallet.userservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public JedisConnectionFactory getJedisConnectionFactory(){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> getRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();

        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setConnectionFactory(getJedisConnectionFactory());

        return redisTemplate;
    }

    @Bean
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }
}
