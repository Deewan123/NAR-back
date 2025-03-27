package com.sdd.response;

import com.sdd.entities.PaymentDetails;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.Timestamp;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class DocterListResponse {


    private String doctorId;
    private String emailId;
    private String mobile;
    private String name;
    private String latitude;
    private String longitude;
    private String hospitalName;
    private String alternateNumber;
    private String address;
    private String totalDistance;

}
