package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "admin_detail")
@Getter
@Setter
public class AdminDetiails {



    @Id
    @Column(name = "admin_id")
    private String adminId;

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

    @Column(name = "is_flag")
    private Integer isFlag;


}
