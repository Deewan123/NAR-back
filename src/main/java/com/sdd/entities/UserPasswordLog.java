package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "user_password_log")
@Getter
@Setter
public class UserPasswordLog {



    @Id
    @Column(name = "user_pass_id")
    private String user_pass_id;

    @Column(name = "password")
    private String password;

    @Column(name = "user_acc_id")
    private String user_acc_id;


    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}
