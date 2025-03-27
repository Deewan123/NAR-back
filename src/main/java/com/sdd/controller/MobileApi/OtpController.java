package com.sdd.controller.MobileApi;

import com.sdd.request.OtpCreateRequest;
import com.sdd.request.OtpVerifyRequest;
import com.sdd.response.ApiResponse;
import com.sdd.response.AppInfoResponse;
import com.sdd.response.OtpResponse;
import com.sdd.service.OtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/otpController")

public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/createOtpMobileApi")
    public ResponseEntity<ApiResponse<OtpResponse>> createOtp(@RequestBody OtpCreateRequest otpCreateRequest){
        ApiResponse<OtpResponse>createOtp = otpService.createOtp(otpCreateRequest);
        return new ResponseEntity<>(createOtp, HttpStatus.OK);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<ApiResponse<OtpResponse>> verifyOtp(@RequestBody OtpVerifyRequest verifyRequest){
      return new ResponseEntity<>(otpService.verfiyOtp(verifyRequest),HttpStatus.OK);
    }


    @PostMapping("/doctorVerifyOtp")
    public ResponseEntity<ApiResponse<OtpResponse>> doctorVerifyOtp(@RequestBody OtpVerifyRequest verifyRequest){
      return new ResponseEntity<>(otpService.doctorVerifyOtp(verifyRequest),HttpStatus.OK);
    }



    @PostMapping("/createDoctorOtpMobileApi")
    public ResponseEntity<ApiResponse<OtpResponse>> createDoctorOtpMobileApi(@RequestBody OtpCreateRequest otpCreateRequest){
        ApiResponse<OtpResponse>createOtp = otpService.createDoctorOtpMobileApi(otpCreateRequest);
        return new ResponseEntity<>(createOtp, HttpStatus.OK);
    }


    @PostMapping("/createOtpMobileApi1")
    public ResponseEntity<ApiResponse<OtpResponse>> createOtp1(@RequestBody OtpCreateRequest otpCreateRequest){
        ApiResponse<OtpResponse>createOtp = otpService.createOtp1(otpCreateRequest);
        return new ResponseEntity<>(createOtp, HttpStatus.OK);
    }


    @PostMapping("/verifyOtp1")
    public ResponseEntity<ApiResponse<OtpResponse>> verifyOtp1(@RequestBody OtpVerifyRequest verifyRequest){
        return new ResponseEntity<>(otpService.verfiyOtp(verifyRequest),HttpStatus.OK);
    }

    @GetMapping("/appInformation")
    public ResponseEntity<ApiResponse<AppInfoResponse>> appInformation(){
        return new ResponseEntity<>(otpService.appInformation(),HttpStatus.OK);
    }

    @GetMapping("/appDocterInformation")
    public ResponseEntity<ApiResponse<AppInfoResponse>> appDocterInformation(){
        return new ResponseEntity<>(otpService.appDocterInformation(),HttpStatus.OK);
    }



    @PostMapping("/createOtpMobileApi11")
    public ResponseEntity<ApiResponse<OtpResponse>> createOtp11(@RequestBody OtpCreateRequest otpCreateRequest){
        ApiResponse<OtpResponse>createOtp = otpService.createOtp11(otpCreateRequest);
        return new ResponseEntity<>(createOtp, HttpStatus.OK);
    }


    @PostMapping("/verifyOtp11")
    public ResponseEntity<ApiResponse<OtpResponse>> verifyOtp11(@RequestBody OtpVerifyRequest verifyRequest){
        return new ResponseEntity<>(otpService.verfiyOtp11(verifyRequest),HttpStatus.OK);
    }



}
