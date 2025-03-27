package com.sdd.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sdd.jwt.HeaderUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "complaint")
@Getter
@Setter
public class Complaint {


    @Id
    @Column(name = "complaint_id")
    private String complaintId;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


    @Column(name = "is_done")
    private Integer isDone;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "breed_id")
    @JsonBackReference
    private CodeBreedType breedId;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "photo_url2")
    private String photoUrl2;

    @Column(name = "photo_url3")
    private String photoUrl3;

    @Column(name = "photo_url4")
    private String photoUrl4;

    @Column(name = "details_of_complainer")
    private String detailsOfComplainer;

    @Column(name = "fine_amount")
    private String fineAmount;

    @Column(name = "name_of_complainer")
    private String nameOfComplainer;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "pet_name")
    private String petName;

    @Column(name = "reason")
    private String reason;

    @Column(name = "status")
    private String status;


    @Column(name = "comp_user_id")
    private String compUserId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cat_id")
    @JsonBackReference
    private CodeFromCategory catId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private CustomerDetail userId;

    @Column(name = "add_line1")
    private String addLine1;

    @Column(name = "address_type")
    private String addressType;

    @Column(name = "block")
    private String block;

    @Column(name = "flat_no")
    private String flatNo;

    @Column(name = "name")
    private String name;

    @Column(name = "landmark")
    private String landmark;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "sector_other")
    private String sectorOther;

    @Column(name = "society_other")
    private String societyOther;

    @Column(name = "street_no")
    private String streetNo;

    @Column(name = "tower_no")
    private String towerNo;

    @Column(name = "village_other")
    private String villageOther;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sector_id")
    @JsonBackReference
    private CodeAddressSector sectorId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "village_id")
    @JsonBackReference
    private CodeAddressVillage village_id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_society_id")
    @JsonBackReference
    private MangerScoiety manageSocietyId;


}
