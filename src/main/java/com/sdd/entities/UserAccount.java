package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.sql.Timestamp;

@Entity
@Table(name = "user_account")
@Getter
@Setter
public class UserAccount {



    @Id
    @Column(name = "user_acc_id")
    private String userAccId;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "is_flag")
    private Integer isFlag;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_block_to")
    private Timestamp isBlockTo;

    @Column(name = "last_login_dt")
    private Timestamp lastLoginDt;

    @Column(name = "change_pwd_dt")
    private Timestamp changePwdDt;

    @Column(name = "login_count")
    private BigInteger login_count;

    @Column(name = "enabled", columnDefinition = "BOOLEAN")
    private Boolean enabled;

    @Column(name = "credentials_expired", columnDefinition = "BOOLEAN")
    private Boolean credentials_expired;

    @Column(name = "account_expired", columnDefinition = "BOOLEAN")
    private Boolean account_expired;

    @Column(name = "account_locked", columnDefinition = "BOOLEAN")
    private Boolean account_locked;

    @Column(name = "mail_id")
    private String mailId;

    @Column(name = "password")
    private String password;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "username")
    private String userName;

    @Column(name = "verif_code")
    private String verifCode;


}
