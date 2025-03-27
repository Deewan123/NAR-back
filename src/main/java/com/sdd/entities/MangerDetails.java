package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "manager_detail")
@Getter
@Setter
public class MangerDetails {



    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "contact_no")
    private String contact_no;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "pwd_deco")
    private String pwd_deco;

    @Column(name = "society_id")
    private String society_id;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}
