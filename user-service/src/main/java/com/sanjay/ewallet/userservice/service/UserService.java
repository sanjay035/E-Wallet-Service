package com.sanjay.ewallet.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjay.ewallet.userservice.constants.ErrorCodes;
import com.sanjay.ewallet.userservice.constants.KafkaConstants;
import com.sanjay.ewallet.userservice.constants.RedisConstants;
import com.sanjay.ewallet.userservice.exception.FinalException;
import com.sanjay.ewallet.userservice.model.User;
import com.sanjay.ewallet.userservice.repository.UserRepository;
import com.sanjay.ewallet.userservice.request.UserRequest;
import com.sanjay.ewallet.userservice.response.UserResponse;
import com.sanjay.ewallet.userservice.util.LoggerWrapper;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
public class UserService {

    private static final Logger LOG = LoggerWrapper.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${redis.key.expiry-in-hours}")
    private int redisKeyExpiryInHours;

    public UserResponse createUser(final UserRequest userRequest) throws FinalException {
        try {
            User user = userRequest.toUser();
            userRepository.save(user);

            // Caching in Redis
            cacheInRedis(userRequest.getUserId(), user);

            // Publishing in Kafka to create wallet
            JSONObject createWalletRequest = new JSONObject();
            createWalletRequest.put("userId", userRequest.getUserId());
            kafkaTemplate.send(KafkaConstants.KAFKA_CREATE_WALLET_TOPIC,
                    userRequest.getUserId(),
                    objectMapper.writeValueAsString(createWalletRequest)
            );

            return user.toUserResponse();
        } catch (Exception e) {
            throw new FinalException(ErrorCodes.CLIENT_ERROR, e.getMessage());
        }
    }

    public UserResponse getUserById(final String userId) throws FinalException{
        try{
            // Checking in Redis if user is cached
            Map userMap = redisTemplate.opsForHash().entries(RedisConstants.REDIS_KEY_PREFIX + userId);
            User user;

            if(userMap == null || userMap.size() == 0) {
                user = userRepository.findByUserId(userId);
                if(user == null) {
                    throw new FinalException(ErrorCodes.CLIENT_ERROR, ErrorCodes.USER_NOT_FOUND);
                }
                cacheInRedis(userId, user);
            }
            else {
                user = objectMapper.convertValue(userMap, User.class);
            }

            return user.toUserResponse();
        } catch (Exception e) {
            throw new FinalException(ErrorCodes.CLIENT_ERROR, e.getMessage());
        }
    }

    public void cacheInRedis(String userId, User user){
        String redisKey = RedisConstants.REDIS_KEY_PREFIX + userId;
        Map userMap = objectMapper.convertValue(user, Map.class);
        redisTemplate.opsForHash().putAll(redisKey, userMap);
        redisTemplate.expire(redisKey, Duration.ofHours(redisKeyExpiryInHours));
    }
}
