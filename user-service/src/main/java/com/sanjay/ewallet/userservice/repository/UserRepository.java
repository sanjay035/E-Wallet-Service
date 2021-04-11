package com.sanjay.ewallet.userservice.repository;

import com.sanjay.ewallet.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(String userId);
}