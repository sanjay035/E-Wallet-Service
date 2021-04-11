package com.sanjay.ewallet.userservice.model;

import com.sanjay.ewallet.userservice.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String userId;

    private String emailId;
    private String name;
    private int age;

    public UserResponse toUserResponse(){
        UserResponse userResponse = UserResponse.builder()
                .userId(this.userId)
                .emailId(this.emailId)
                .name(this.name)
                .age(this.age)
                .build();
        return userResponse;
    }
}
