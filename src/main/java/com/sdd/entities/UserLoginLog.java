package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "user_login_log")
@Getter
@Setter
public class UserLoginLog {



    @Id
    @Column(name = "log_id")
    private String logId;

    @Column(name = "status")
    private String status;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_acc_id")
    private String userAccId;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "log_out_date")
    private Timestamp logOutDate;

    @Column(name = "log_in_date")
    private Timestamp logInDate;

    @Column(name = "is_block_to")
    private Timestamp isBlockTo;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;

    @Column(name = "is_block_count")
    private Integer isBlockCount;


}
