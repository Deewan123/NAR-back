package com.sdd.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
public class CreateUserRequest {

    private String mailId;
    private String mobileNo;
    private String password;
    private String userType;
    private String userName;
}
