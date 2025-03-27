package com.sdd.request;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Getter
@Setter
public class MainFormRequest {



    private String formId;
    private String amount;
    private String approverRemark;
    private String certId;
    private String cerFileUrl;
    private String approverState;
    private String address;
    private String approveDate;
    private String createdOn;
    private String updatedOn;
    private String isFlag;
    private String creatorState;
    private String dob;
    private String dogDetailId;
    private String dogId;
    private String expiryDate;
    private String formRemark;
    private String formType;
    private String sex;
    private String gurdianName;
    private String isRenewed;
    private String nextRabbiesDate;
    private String oldCertfNoDate;
    private String rabbiesDate;
    private String nickName;
    private String transactionData;
    private String txnId;
    private String validMonth;
    private String addLine1;
    private String sectorOther;
    private String manageSocietyId;
    private String sectorId;
    private String villageOther;
    private String villageId;
    private String towerNo;
    private String streetNo;
    private String societyOther;
    private String pincode;
    private String landmark;
    private String flatNo;
    private String block;
    private String verifierState;
    private String addressType;
    private String verifierId;
    private String creatorId;
    private String catId;
    private String breedId;
    private String approverId;
    private String verifierRemark;
    private String newRegistrationNo;
    private String oldRegistrationNo;
    private String orderId;
    private String isNeutering;
    private String ownerName;
    private String ownerNumber;
    private String paymentStatus;



}
