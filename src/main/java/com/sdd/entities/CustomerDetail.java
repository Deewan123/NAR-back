package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Entity
@Table(name = "customer_detail")
@Getter
@Setter
public class CustomerDetail {

    @Id
    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "address")
    private String address;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "name")
    private String name;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;

    @Column(name = "cert_count")
    private Integer certCount = 0;

    @Column(name = "dog_count")
    private Integer dogCount;

    @Column(name = "is_status")
    private Integer isStatus;

    @Column(name = "is_verified")
    private Integer isVerified;


}
