package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "dog_detail_main_form")
@Getter
@Setter
public class DogDetailMainForm {



    @Id
    @Column(name = "dog_detail_main_form")
    private String dogDetailMainForm;

    @Column(name = "expiry_date")
    private Timestamp expiryDate;

    @Column(name = "dog_detail_id")
    private String dogDetailId;

    @Column(name = "form_id")
    private String formId;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}
