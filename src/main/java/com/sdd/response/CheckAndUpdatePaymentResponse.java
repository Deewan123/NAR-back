package com.sdd.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdd.entities.PaymentDetails;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class CheckAndUpdatePaymentResponse {

    private String msg;


}
