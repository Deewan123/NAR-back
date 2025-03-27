package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Entity
@Table(name = "doctor_detail")
@Getter
@Setter
public class DocterDetiails {


    @Id
    @Column(name = "doctor_id")
    private String doctorId;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "name")
    private String name;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "alternate_number")
    private String alternateNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "otp")
    private String otp;

    @Column(name = "on_Off")
    private Integer onOff;
}
