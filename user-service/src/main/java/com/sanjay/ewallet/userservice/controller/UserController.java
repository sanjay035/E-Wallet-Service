package com.sanjay.ewallet.userservice.controller;

import com.sanjay.ewallet.userservice.exception.FinalException;
import com.sanjay.ewallet.userservice.service.UserService;
import com.sanjay.ewallet.userservice.util.ResponseGenerator;
import com.sanjay.ewallet.userservice.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@RequestBody UserRequest userRequest) throws FinalException {
        return new ResponseEntity<>(ResponseGenerator.okResponse(userService.createUser(userRequest)), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getUserById(@RequestParam("userId") String userId) throws FinalException {
        return new ResponseEntity<>(ResponseGenerator.okResponse(userService.getUserById(userId)), HttpStatus.OK);
    }
}
