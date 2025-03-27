package com.sdd.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigInteger;
import java.sql.Timestamp;


@Getter
@Setter
@ToString
public class LoginResponse {



    private Timestamp lastLoginDt;
    private String setToken;
    private String mailId;
    private String userId;
    private String logId;
    private String userType;
    private String userName;
}
