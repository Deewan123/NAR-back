package com.sdd.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdatePaymentRequest {



    private String formId;
    private String isNeutering;
    private String txnData;
//    private String name;
//    private String phoneNO;
//    private String amount;
    private String orderId;



}
