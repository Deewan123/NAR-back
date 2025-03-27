package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.sql.Timestamp;

@Entity
@Table(name = "staff_detail")
@Getter
@Setter
//@XmlAccessorType(XmlAccessType.FIELD)
public class StaffDetails {



    @Id
    @Column(name = "staff_id")
    private String staff_id;

    @Column(name = "email_id")
    private String emailId;

//    @NotNull(message = "THE MOBILE NUMBER MUST BE 10 DIGITS")
//    @NotEmpty(message = "MOBILE NUMBER CAN NOT BE BLANK")
    @Column(name = "mobile")
    private String mobile;


//    @NotNull(message = "NAME CAN NOT BE NULL")
//    @NotEmpty(message = "NAME CAN NOT BE BLANK")
    @Column(name = "name")
    private String name;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}
