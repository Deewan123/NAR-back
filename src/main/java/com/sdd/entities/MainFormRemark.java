package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "main_form_remark")
@Getter
@Setter
public class MainFormRemark {



    @Id
    @Column(name = "form_remark_id")
    private String form_remark_id;

    @Column(name = "user_type")
    private String user_type;

    @Column(name = "form_id")
    private String form_id;

    @Column(name = "form_remark")
    private Boolean form_remark;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}
