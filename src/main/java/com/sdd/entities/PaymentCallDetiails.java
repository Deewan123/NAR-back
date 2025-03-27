package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Entity
@Table(name = "payment_callback")
@Getter
@Setter
public class PaymentCallDetiails {



    @Id
    @Column(name = "payment_callback_id")
    private String id;

    @Column(name = "payment_callback_response")
    private String response;


    @Column(name = "order_id")
    private String orderId;


    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;


}
