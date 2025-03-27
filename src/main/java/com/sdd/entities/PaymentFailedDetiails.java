package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Entity
@Table(name = "payment_failed_details")
@Getter
@Setter
public class PaymentFailedDetiails {


    @Id
    @Column(name = "detailedId")
    private String detailedId;

    @Column(name = "name")
    private String name;

    @Column(name = "isPayment")
    private String isPayment;

    @Column(name = "response")
    private String response;


    @Column(name = "paymentStatus")
    private String paymentStatus;

    @Column(name = "mobileNumber")
    private String mobileNumber;

    @Column(name = "transId")
    private String transId;


    @Column(name = "isChecked")
    private String isChecked;


    @Column(name = "orderId")
    private String orderId;


    @Column(name = "createdOn")
    private Timestamp createdOn;

    @Column(name = "updatedOn")
    private Timestamp updatedOn;


}
