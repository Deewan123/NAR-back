package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Entity
@Table(name = "trans_msg_detail")
@Getter
@Setter
public class TransMsgDetiails {


    @Id
    @Column(name = "msg_id")
    private String adminId;

    @Column(name = "msg")
    private String emailId;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}