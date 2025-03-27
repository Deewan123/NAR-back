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
public class AllUserResponse {

    private Timestamp lastLoginDt;
    private String mailId;
    private String userId;
    private String userType;
    private String userName;
    private String userAccId;
    private Timestamp createdOn;
    private Timestamp updatedOn;
    private Timestamp isBlockTo;
    private BigInteger login_count;
    private Boolean enabled;
    private Boolean credentials_expired;
    private Boolean account_expired;
    private Boolean account_locked;

}
