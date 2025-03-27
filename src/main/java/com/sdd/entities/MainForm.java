package com.sdd.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "main_form")
@Getter
@Setter
public class MainForm {



    @Id
    @Column(name = "form_id")
    private String formId;

    @Column(name = "amount")
    private String amount;

    @Column(name = "approver_remark")
    private String approverRemark;

    @Column(name = "cert_id")
    private String certId;

    @Column(name = "cer_file_url")
    private String cerFileUrl;

    @Column(name = "approver_state")
    private String approverState;

    @Column(name = "address")
    private String address;

    @Column(name = "approve_date")
    private Timestamp approveDate;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;

    @Column(name = "creator_state")
    private String creatorState;

    @Column(name = "dob")
    private String dob;

    @Column(name = "dog_detail_id")
    private String dogDetailId;

    @Column(name = "dog_id")
    private String dogId;

    @Column(name = "expiry_date")
    private Timestamp expiryDate;

    @Column(name = "form_remark")
    private String formRemark;

    @Column(name = "form_type")
    private String formType;

    @Column(name = "sex")
    private String sex;

    @Column(name = "gurdian_name")
    private String gurdianName;

    @Column(name = "is_renewed")
    private Integer isRenewed;

    @Column(name = "next_rabbies_date")
    private Timestamp nextRabbiesDate;

    @Column(name = "old_certf_no_date")
    private Timestamp oldCertfNoDate;

    @Column(name = "rabbies_date")
    private Timestamp rabbiesDate;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "transaction_data")
    private String transactionData;

    @Column(name = "txn_id")
    private String txnId;

    @Column(name = "valid_month")
    private String validMonth;

    @Column(name = "add_line1")
    private String addLine1;

    @Column(name = "sector_other")
    private String sectorOther;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_society_id")
    @JsonBackReference
    private MangerScoiety manageSocietyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sector_id")
    @JsonBackReference
    private CodeAddressSector sectorId;

    @Column(name = "village_other")
    private String villageOther;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "village_id")
    @JsonBackReference
    private CodeAddressVillage villageId;

    @Column(name = "tower_no")
    private String towerNo;;

    @Column(name = "street_no")
    private String streetNo;

    @Column(name = "society_other")
    private String societyOther;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "landmark")
    private String landmark;

    @Column(name = "flat_no")
    private String flatNo;

    @Column(name = "block")
    private String block;

    @Column(name = "verifier_state")
    private String verifierState;

    @Column(name = "address_type")
    private String addressType;

    @Column(name = "verifier_id")
    private String verifierId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    @JsonBackReference
    private UserAccount creatorId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cat_id")
    @JsonBackReference
    private CodeFromCategory catId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "breed_id")

    @JsonBackReference
    private CodeBreedType breedId;

    @Column(name = "approver_id")
    private String approverId;

    @Column(name = "verifier_remark")
    private String verifierRemark;

    @Column(name = "new_registration_no")
    private String newRegistrationNo;

    @Column(name = "old_registration_no")
    private String oldRegistrationNo;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "owner_number")
    private String ownerNumber;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "isUpdate")
    private String isUpdate;

    @Column(name = "isPendingList")
    private String isPendingList;


    @Column(name = "isNeutering")
    private String isNeutering;



}
