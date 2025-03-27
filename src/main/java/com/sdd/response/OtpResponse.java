package com.sdd.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtpResponse {
    private String message;
    private String userName;
//    private String otpNumber;
    private String tokenNo;
    private String cretedId;
    private boolean status;
}
