package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "form_renewal")
@Getter
@Setter
public class FormRenewal {



    @Id
    @Column(name = "renew_id")
    private String renew_id;

    @Column(name = "form_id")
    private String form_id;

    @Column(name = "renew_status")
    private String renew_status;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}
