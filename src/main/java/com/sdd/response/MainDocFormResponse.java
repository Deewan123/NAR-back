package com.sdd.response;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Getter
@Setter
public class MainDocFormResponse {

    private String doc_form_id= "";
    private String offline_payment_slip= "";
    private String upload_dog1_url= "";
    private String upload_dog2_url= "";
    private String upload_dog3_url= "";
    private String upload_id_proof= "";
    private String upload_id_url= "";
    private String upload_photo_url= "";
    private String upload_sign_url= "";
    private String upload_valid_book= "";
    private String upload_valid_book2= "";
    private String doc_type_id= "";
    private String formId;
    private String upload_id_proof_back= "";
    private Timestamp createdOn;
    private Timestamp updatedOn;
    private Integer isFlag =0 ;


}
