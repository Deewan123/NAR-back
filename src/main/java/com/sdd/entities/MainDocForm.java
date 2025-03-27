package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "main_doc_form")
@Getter
@Setter
public class MainDocForm {


    @Id
    @Column(name = "doc_form_id")
    private String docFormId;

    @Column(name = "offline_payment_slip")
    private String offline_payment_slip;

    @Column(name = "upload_dog1_url")
    private String upload_dog1_url;

    @Column(name = "upload_dog2_url")
    private String upload_dog2_url;

    @Column(name = "upload_dog3_url")
    private String upload_dog3_url;

    @Column(name = "upload_id_proof")
    private String upload_id_proof;

    @Column(name = "upload_id_url")
    private String upload_id_url;

    @Column(name = "upload_photo_url")
    private String upload_photo_url;

    @Column(name = "upload_sign_url")
    private String upload_sign_url;

    @Column(name = "upload_valid_book")
    private String upload_valid_book;

    @Column(name = "upload_valid_book2")
    private String upload_valid_book2;

    @Column(name = "doc_type_id")
    private String doc_type_id;

    @Column(name = "form_id")
    private String formId;

    @Column(name = "upload_id_proof_back")
    private String upload_id_proof_back;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag = 0;


}
