package com.sdd.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerifyRequest {
    private String otp;
    private String mobileNo;
}
