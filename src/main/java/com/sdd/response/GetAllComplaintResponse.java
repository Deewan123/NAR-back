package com.sdd.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.Timestamp;


@Getter
@Setter
@ToString
public class GetAllComplaintResponse {

    private String complaintId;
    private String ownerName;
    private String reason;
    private Timestamp createdOn;
    private String status;
    private String detailsOfComplainer;
    private String nameOfComplainer;

}
