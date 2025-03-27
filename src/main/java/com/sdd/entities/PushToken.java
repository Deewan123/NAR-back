package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "push_tokens")
@Getter
@Setter
public class PushToken {


    @Id
    @Column(name = "push_id")
    private String push_id;

    @Column(name = "customer_id")
    private String customer_id;

    @Column(name = "device_id")
    private String device_id;

    @Column(name = "fcm_token")
    private String fcm_token;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}
