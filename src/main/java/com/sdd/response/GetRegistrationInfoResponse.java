package com.sdd.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;


@Getter
@Setter
@ToString
public class GetRegistrationInfoResponse {




    private String certificateNo;
    private String OwnerName;
    private String PetNAme;
    private Timestamp rabiesVaccinationDate;
    private Timestamp nextRabiesVaccinationDate;
    private Timestamp certificateExpired;
    private String ownerPhotoUrl;


}
