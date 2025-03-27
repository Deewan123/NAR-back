package com.sdd.response;

import com.sdd.entities.PaymentDetails;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class DocterListRequest {


    private String latitude;
    private String longitude;
}
