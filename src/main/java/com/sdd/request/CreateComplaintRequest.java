package com.sdd.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateComplaintRequest {

    private String pincode;
    private String landmark;
    private String addressLine1;
    private String flatNumber;
    private String townNumber;
    private String sectorNumber;
    private String societyNumber;
    private String sectorOther;
    private String societyOther;
    private String addressType;
    private String catId;
    private String breedId;

    private String villageId;
    private String villageOther;
    private String detailsOfComplainer;
    private String reason;
    private String block;
    private String petName;
    private String ownerName;
    private String petType;
    private String breedType;
    private String photoUrl;
    private String photoUrl2;
    private String photoUrl3;
    private String photoUrl4;
    private String complainerName;
}
