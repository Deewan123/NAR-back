package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "payment_detail")
@Getter
@Setter
public class PaymentDetails {



    @Id
    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "amount")
    private String amount;

    @Column(name = "check_sum")
    private String checkSum;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "txn_id")
    private String txnId;

    @Column(name = "product_info")
    private String productInfo;

    @Column(name = "phone")
    private String phone;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "pay_id")
    private String payId;

    @Column(name = "payment_date")
    private Timestamp paymentDate;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}
