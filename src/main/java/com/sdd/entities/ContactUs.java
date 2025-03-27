package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "contact_us")
@Getter
@Setter
public class ContactUs {



    @Id
    @Column(name = "contact_us_id")
    private String contact_us_id;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "message")
    private String message;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_no")
    private String phone_no;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}
