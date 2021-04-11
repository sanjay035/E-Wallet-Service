package com.sanjay.ewallet.userservice.request;

import com.sanjay.ewallet.userservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserRequest {

    private String userId;
    private String emailId;
    private String name;
    private int age;

    public User toUser(){
        User user = User.builder()
                .userId(this.getUserId())
                .emailId(this.getEmailId())
                .name(this.getName())
                .age(this.getAge())
                .build();
        return user;
    }
}
