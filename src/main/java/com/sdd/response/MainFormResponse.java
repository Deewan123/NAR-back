package com.sdd.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdd.entities.PaymentDetails;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class MainFormResponse {

    private String formId;
    private String amount;
    private String approverRemark;
    private String certId;
    private String cerFileUrl;
    private String approverState;
    private String address;
    private Timestamp approveDate;
    private Timestamp createdOn;
    private Timestamp updatedOn;
    private Integer isFlag;
    private String creatorState;
    private String dob;
    private String dogDetailId;
    private String dogId;
    private Timestamp expiryDate;
    private String formRemark;
    private String formType;
    private String sex;
    private String gurdianName;
    private Integer isRenewed;
    private Timestamp nextRabbiesDate;
    private Timestamp oldCertfNoDate;
    private Timestamp rabbiesDate;
    private String nickName;
    private PaymentDetails transactionData;
    private String txnId;
    private String validMonth;
    private String addLine1;
    private String sectorOther;
    private String manageSocietyId;
    private String sectorId;
    private String villageOther;
    private String villageId;
    private String towerNo;;
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
    private String breedName;
    private String approverId;
    private String verifierRemark;
    private String newRegistrationNo;
    private String oldRegistrationNo;
    private String orderId;
    private String ownerName;
    private String ownerNumber;
    private String paymentStatus;
    private String isNeutering;

    MainDocFormResponse mainDocFormResponse;
    private String petType;
    private String registrationExpire;
    private String dayRemaining;
    private String penaltyAmount;
    private String docId;
}
