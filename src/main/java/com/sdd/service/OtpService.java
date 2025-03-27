package com.sdd.service;

import com.sdd.request.OtpCreateRequest;
import com.sdd.request.OtpVerifyRequest;
import com.sdd.response.ApiResponse;
import com.sdd.response.AppInfoResponse;
import com.sdd.response.OtpResponse;

public interface OtpService {


    ApiResponse<OtpResponse> createOtp(OtpCreateRequest otpCreateRequest);
    ApiResponse<OtpResponse> verfiyOtp(OtpVerifyRequest otpCreateRequest);
    ApiResponse<AppInfoResponse> appInformation();
    ApiResponse<AppInfoResponse> appDocterInformation();

    ApiResponse<OtpResponse> createOtp1(OtpCreateRequest otpCreateRequest);
    ApiResponse<OtpResponse> verfiyOtp1(OtpVerifyRequest otpCreateRequest);

    ApiResponse<OtpResponse> createOtp11(OtpCreateRequest otpCreateRequest);
    ApiResponse<OtpResponse> verfiyOtp11(OtpVerifyRequest otpCreateRequest);


    ApiResponse<OtpResponse> createDoctorOtpMobileApi(OtpCreateRequest otpCreateRequest);

    ApiResponse<OtpResponse> doctorVerifyOtp(OtpVerifyRequest verifyRequest);
}
