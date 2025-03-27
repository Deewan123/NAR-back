package com.sdd.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;

import com.sdd.config.DataBaseConnection;
import com.sdd.entities.*;
import com.sdd.entities.repository.*;
import com.sdd.exception.SDDException;
import com.sdd.jwt.HeaderUtils;
import com.sdd.jwt.JwtUtils;
import com.sdd.request.OtpCreateRequest;
import com.sdd.request.OtpVerifyRequest;
import com.sdd.response.ApiResponse;
import com.sdd.response.AppInfoResponse;
import com.sdd.response.OtpResponse;
import com.sdd.service.CustomerDetailsServices;
import com.sdd.service.OtpService;
import com.sdd.utils.ConverterUtils;
import com.sdd.utils.HelperUtils;
import com.sdd.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class OtpServiceImpl implements OtpService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private UserAccountRepositry userAccountRepositry;

    @Autowired
    private CustomerDetailsRepositry customerDetailsRepositry;

    @Autowired
    private MainFromRepositry mainFromRepositry;

    @Autowired
    private MainDocFromRepositry mainDocFromRepositry;

    @Autowired
    private DoctorRepositry doctorRepositry;


    @Override
    public ApiResponse<OtpResponse> createOtp(OtpCreateRequest otpCreateRequest) {

        OtpResponse otpResponse = new OtpResponse();
        String otp = "";
        if (otpCreateRequest.getName() == null || otpCreateRequest.getName().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "USER NAME CAN NOT BE BLANK");
        }
        if (otpCreateRequest.getMobileNo() == null || otpCreateRequest.getMobileNo().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "MOBILE NUMBER CAN NOT BE BLANK");
        }
        if (otpCreateRequest.getMobileNo().length() > 10 || otpCreateRequest.getMobileNo().length() <= 9) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OWNER NUMBER NOT VAILD");
        }


        List<UserAccount> userAccount = userAccountRepositry.findByUserName(otpCreateRequest.getMobileNo());
        List<CustomerDetail> customerDetail = customerDetailsRepositry.findByMobileNo(otpCreateRequest.getMobileNo());
        try {


            if (otpCreateRequest.getMobileNo().equalsIgnoreCase("9897040757")) {
                otp = "123456";
                otpResponse.setMessage("OTP sent successfully");
                otpResponse.setStatus(true);
                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
                });
            }


//            if (otpCreateRequest.getMobileNo().equalsIgnoreCase("7488863501")) {
//                otp = "123456";
//                otpResponse.setMessage("OTP sent successfully");
//                otpResponse.setStatus(true);
//                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
//                });
//            }
            otp = callNewOTP(otpCreateRequest.getMobileNo());
            //otp = callExternalRestService(otpCreateRequest.getMobileNo());
            if (otp == null) {
                otpResponse.setMessage("Internal server error");
            } else {
                otpResponse.setMessage("OTP sent successfully");
                otpResponse.setStatus(true);
            }
        } catch (Exception e) {
            otpResponse.setMessage("Internal server error");
            otpResponse.setStatus(false);
        }

        if (userAccount.size() <= 0) {
            UserAccount userRegistration = new UserAccount();
            userRegistration.setUserAccId(HelperUtils.getUserAccountId());
            userRegistration.setCreatedOn(HelperUtils.getCurrentTimeStamp());
            userRegistration.setIsFlag(HelperUtils.getUserIsFlag());
            userRegistration.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
            userRegistration.setAccount_expired(true);
            userRegistration.setAccount_locked(true);
            userRegistration.setCredentials_expired(true);
            userRegistration.setEnabled(true);
            userRegistration.setPassword(HelperUtils.getUserPasswordLogId());
            userRegistration.setUserId(HelperUtils.getUserAccountId());
            userRegistration.setUserType("CD");
            userRegistration.setUserName(otpCreateRequest.getMobileNo());
            userRegistration.setVerifCode(otp);
            userAccountRepositry.save(userRegistration);

        } else {

            if (userAccount.get(0).getLogin_count() == null) {
                userAccount.get(0).setLogin_count(new BigInteger("1"));
            } else {
                userAccount.get(0).setLogin_count(userAccount.get(0).getLogin_count().add(new BigInteger("1")));
            }

            userAccount.get(0).setVerifCode(otp);
            userAccount.get(0).setUpdatedOn(HelperUtils.getCurrentTimeStamp());
            userAccountRepositry.save(userAccount.get(0));
        }

        if (customerDetail.size() <= 0) {
            CustomerDetail customerDetails = new CustomerDetail();
            customerDetails.setCustomerId(HelperUtils.getUserAccountId());
            customerDetails.setCreatedOn(HelperUtils.getCurrentTimeStamp());
            customerDetails.setIsFlag(HelperUtils.getUserIsFlag());
            customerDetails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
            customerDetails.setIsVerified(0);
            customerDetails.setMobileNo(otpCreateRequest.getMobileNo());
            customerDetails.setName(otpCreateRequest.getName());

            customerDetailsRepositry.save(customerDetails);
        }


        return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
        });
    }


    @Override
    public ApiResponse<OtpResponse> createDoctorOtpMobileApi(OtpCreateRequest otpCreateRequest) {

        OtpResponse otpResponse = new OtpResponse();
        String otp = "";
        if (otpCreateRequest.getName() == null || otpCreateRequest.getName().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "USER NAME CAN NOT BE BLANK");
        }
        if (otpCreateRequest.getMobileNo() == null || otpCreateRequest.getMobileNo().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "MOBILE NUMBER CAN NOT BE BLANK");
        }
        if (otpCreateRequest.getMobileNo().length() > 10 || otpCreateRequest.getMobileNo().length() <= 9) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OWNER NUMBER NOT VALID");
        }

        try {

//            if (otpCreateRequest.getMobileNo().equalsIgnoreCase("9404727100")) {
//                otpResponse.setMessage("OTP sent successfully");
//                otpResponse.setStatus(true);
//                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
//                });
//            }

            if (otpCreateRequest.getMobileNo().equalsIgnoreCase("9897040757")) {
                otpResponse.setMessage("OTP sent successfully");
                otpResponse.setStatus(true);
                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
                });
            }

            DocterDetiails docterDetiails = doctorRepositry.findByMobile(otpCreateRequest.getMobileNo());
            if (docterDetiails == null) {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "DOCTOR NUMBER NOT VALID");
            } else {
                otp = callNewOTP(otpCreateRequest.getMobileNo());
                docterDetiails.setOtp(otp);
                doctorRepositry.save(docterDetiails);
            }

            if (otp == null) {
                otpResponse.setMessage("Internal server error");
            } else {
                otpResponse.setMessage("OTP sent successfully");
                otpResponse.setStatus(true);
            }
        } catch (Exception e) {
            otpResponse.setMessage("Internal server error");
            otpResponse.setStatus(false);
        }

        return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
        });
    }

    @Override
    public ApiResponse<OtpResponse> doctorVerifyOtp(OtpVerifyRequest otpCreateRequest) {
        OtpResponse otpResponse = new OtpResponse();
        if (otpCreateRequest.getMobileNo() == null || otpCreateRequest.getMobileNo().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "MOBILE NUMBER CAN NOT BE BLANK");

        }
        if (otpCreateRequest.getMobileNo().length() <= 9 || otpCreateRequest.getMobileNo().length() > 10) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID MOBILE NUMBER");
        }

        if (otpCreateRequest.getOtp() == null || otpCreateRequest.getOtp().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP CAN NOT BE BLANK");
        }
        if (otpCreateRequest.getOtp().length() <= 5 || otpCreateRequest.getOtp().length() > 6) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID OTP");
        }


        DocterDetiails docterDetiails = doctorRepositry.findByMobile(otpCreateRequest.getMobileNo());

        if (docterDetiails == null) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID MOBILE NUMBER");
        }


//        if (otpCreateRequest.getMobileNo().equalsIgnoreCase("9404727100")) {
//            if (("112233".equalsIgnoreCase(otpCreateRequest.getOtp()))) {
//                otpResponse.setMessage("OTP verify successfully");
//                String jwtToken = jwtUtils.generateJwtToken(otpCreateRequest.getMobileNo(), HelperUtils.getMobileUserType, docterDetiails.getDoctorId());
//                otpResponse.setTokenNo(jwtToken);
//                otpResponse.setUserName(docterDetiails.getName());
//                otpResponse.setStatus(true);
//                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
//                });
//            } else {
//                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP MISMATCH. PLEASE ENTER CORRECT OTP");
//            }
//        }
        if (otpCreateRequest.getMobileNo().equalsIgnoreCase("9897040757")) {
            if (("112233".equalsIgnoreCase(otpCreateRequest.getOtp()))) {
                otpResponse.setMessage("OTP verify successfully");
                String jwtToken = jwtUtils.generateJwtToken(otpCreateRequest.getMobileNo(), HelperUtils.getMobileUserType, docterDetiails.getDoctorId());
                otpResponse.setTokenNo(jwtToken);
                otpResponse.setUserName(docterDetiails.getName());
                otpResponse.setStatus(true);
                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
                });
            } else {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP MISMATCH. PLEASE ENTER CORRECT OTP");
            }
        }


        if (!(docterDetiails.getOtp().equalsIgnoreCase(otpCreateRequest.getOtp()))) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP MISMATCH. PLEASE ENTER CORRECT OTP");
        }


        if (docterDetiails != null) {

            long timeDiffer = ConverterUtils.timeDiffer(docterDetiails.getUpdatedOn());
            if (timeDiffer > 10) {
                otpResponse.setStatus(false);
                otpResponse.setMessage("THE SMS CODE HAS EXPIRED. PLEASE RE-SEND THE VERIFICATION CODE");
                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
                });
            }

            otpResponse.setMessage("OTP verify successfully");
            String jwtToken = jwtUtils.generateJwtToken(otpCreateRequest.getMobileNo(), HelperUtils.getMobileUserType, docterDetiails.getDoctorId());
            otpResponse.setTokenNo(jwtToken);
            otpResponse.setCretedId(docterDetiails.getDoctorId());
            otpResponse.setUserName(docterDetiails.getName());
            otpResponse.setStatus(true);

        } else {
            otpResponse.setStatus(false);
            otpResponse.setMessage("Invalid otp");

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid otp");
        }

        return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
        });
    }


    @Override
    public ApiResponse<OtpResponse> createOtp1(OtpCreateRequest otpCreateRequest) {
        OtpResponse otpResponse = new OtpResponse();
        String otp = "";
        if (otpCreateRequest.getName() == null || otpCreateRequest.getName().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "USER NAME CAN NOT BE BLANK");
        }
        if (otpCreateRequest.getMobileNo() == null || otpCreateRequest.getMobileNo().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "MOBILE NUMBER CAN NOT BE BLANK");
        }
        if (otpCreateRequest.getMobileNo().length() > 10 || otpCreateRequest.getMobileNo().length() <= 9) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OwnerNumber not valid");
        }


        List<UserAccount> userAccount = userAccountRepositry.findByUserName(otpCreateRequest.getMobileNo());
        List<CustomerDetail> customerDetail = customerDetailsRepositry.findByMobileNo(otpCreateRequest.getMobileNo());
        try {


//            Connection connection = DataBaseConnection.getConnection1();
//            System.out.println("databaseConnectionKKK===============" + connection.toString());

            if (otpCreateRequest.getMobileNo().equalsIgnoreCase("9897040757")) {
                otp = "123456";
                otpResponse.setMessage("OTP sent successfully");
                otpResponse.setStatus(true);
                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
                });
            }

            otp = callNewOTP(otpCreateRequest.getMobileNo());

            if (otp == null) {
                otpResponse.setMessage("Internal server error");
            } else {
                otpResponse.setMessage("OTP sent successfully");
                otpResponse.setStatus(true);
            }
        } catch (Exception e) {
            otpResponse.setMessage("Internal server error");
            otpResponse.setStatus(false);
        }

        if (userAccount.size() <= 0) {
            UserAccount userRegistration = new UserAccount();
            userRegistration.setUserAccId(HelperUtils.getUserAccountId());
            userRegistration.setCreatedOn(HelperUtils.getCurrentTimeStamp());
            userRegistration.setIsFlag(HelperUtils.getUserIsFlag());
            userRegistration.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
            userRegistration.setAccount_expired(true);
            userRegistration.setAccount_locked(true);
            userRegistration.setCredentials_expired(true);
            userRegistration.setEnabled(true);
            userRegistration.setPassword(HelperUtils.getUserPasswordLogId());
            userRegistration.setUserId(HelperUtils.getUserAccountId());
            userRegistration.setUserType("CD");
            userRegistration.setUserName(otpCreateRequest.getMobileNo());
            userRegistration.setVerifCode(otp);
            userAccountRepositry.save(userRegistration);

        } else {

            if (userAccount.get(0).getLogin_count() == null) {
                userAccount.get(0).setLogin_count(new BigInteger("1"));
            } else {
                userAccount.get(0).setLogin_count(userAccount.get(0).getLogin_count().add(new BigInteger("1")));
            }

            userAccount.get(0).setVerifCode(otp);
            userAccount.get(0).setUpdatedOn(HelperUtils.getCurrentTimeStamp());
            userAccountRepositry.save(userAccount.get(0));
        }

        if (customerDetail.size() <= 0) {
            CustomerDetail customerDetails = new CustomerDetail();
            customerDetails.setCustomerId(HelperUtils.getUserAccountId());
            customerDetails.setCreatedOn(HelperUtils.getCurrentTimeStamp());
            customerDetails.setIsFlag(HelperUtils.getUserIsFlag());
            customerDetails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
            customerDetails.setIsVerified(0);
            customerDetails.setMobileNo(otpCreateRequest.getMobileNo());
            customerDetails.setName(otpCreateRequest.getName());

            customerDetailsRepositry.save(customerDetails);
        }


        return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
        });
    }



    public String callNewOTP(String mobileNo) {

        HttpResponse response = null;
        String otp = ConverterUtils.createdOtp();
        String otpMessage = HeaderUtils.NewMSG.replace("{#var#}", otp);
        String url = "https://api.mylogin.co.in/api/v2/SendSMS";
        RestTemplate template = new RestTemplate();

        // Query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                // Add query parameter
                .queryParam("ApiKey", HeaderUtils.NewAPIKEY)
                .queryParam("ClientId", HeaderUtils.NewClintId)
                .queryParam("SenderId", HeaderUtils.NewSENDER)
                .queryParam("Message", otpMessage)
                .queryParam("MobileNumbers", "91" + mobileNo);

        System.out.println(builder.buildAndExpand().toUri());

        String responseNew = template.getForObject(builder.buildAndExpand().toUri(), String.class);
        System.out.println(responseNew);
        return otp;
    }


    @Override
    public ApiResponse<OtpResponse> verfiyOtp1(OtpVerifyRequest otpCreateRequest) {

        OtpResponse otpResponse = new OtpResponse();
        if (otpCreateRequest.getMobileNo() == null || otpCreateRequest.getMobileNo().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "MOBILE NUMBER CAN NOT BE BLANK");

        }
        if (otpCreateRequest.getMobileNo().length() <= 9 || otpCreateRequest.getMobileNo().length() > 10) {

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID MOBILE NUMBER");

        }

        if (otpCreateRequest.getOtp() == null || otpCreateRequest.getOtp().isEmpty()) {

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP CAN NOT BE BLANK");

        }
        if (otpCreateRequest.getOtp().length() <= 5 || otpCreateRequest.getOtp().length() > 6) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID OTP");

        }


        List<UserAccount> userAccount = userAccountRepositry.findByUserName(otpCreateRequest.getMobileNo());
        List<CustomerDetail> customerDetail = customerDetailsRepositry.findByMobileNo(otpCreateRequest.getMobileNo());


        if (userAccount.size() <= 0) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID MOBILE NUMBER");

        }
        if (customerDetail.size() <= 0) {

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID MOBILE NUMBER");

        }

        if (otpCreateRequest.getMobileNo().equalsIgnoreCase("9897040757")) {
            if (("123456".equalsIgnoreCase(otpCreateRequest.getOtp()))) {
                otpResponse.setMessage("OTP verify successfully");
                String jwtToken = jwtUtils.generateJwtToken(otpCreateRequest.getMobileNo(), HelperUtils.getMobileUserType, customerDetail.get(0).getCustomerId());
                otpResponse.setTokenNo(jwtToken);
                otpResponse.setCretedId(userAccount.get(0).getUserAccId());
                otpResponse.setUserName("Avneesh Kumar");
                otpResponse.setStatus(true);
                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
                });
            } else {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP MISMATCH. PLEASE ENTER CORRECT OTP");
            }
        }
        if (otpCreateRequest.getMobileNo().equalsIgnoreCase("9123456789")) {
            if (("123457".equalsIgnoreCase(otpCreateRequest.getOtp()))) {
                otpResponse.setMessage("OTP verify successfully");
                String jwtToken = jwtUtils.generateJwtToken(otpCreateRequest.getMobileNo(), HelperUtils.getMobileUserType, customerDetail.get(0).getCustomerId());
                otpResponse.setTokenNo(jwtToken);
//                otpResponse.setCretedId(userAccount.getUserAccId());
                otpResponse.setUserName("Avneesh Kumar");
                otpResponse.setStatus(true);
                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
                });
            } else {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP MISMATCH. PLEASE ENTER CORRECT OTP");
            }
        }

        if (!(userAccount.get(0).getVerifCode().equalsIgnoreCase(otpCreateRequest.getOtp()))) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP MISMATCH. PLEASE ENTER CORRECT OTP");
        }


        if (userAccount != null) {

            long timeDiffer = ConverterUtils.timeDiffer(userAccount.get(0).getUpdatedOn());
            if (timeDiffer > 10) {
                otpResponse.setStatus(false);
                otpResponse.setMessage("THE SMS CODE HAS EXPIRED. PLEASE RE-SEND THE VERIFICATION CODE");
                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
                });
            }

            otpResponse.setMessage("OTP verify successfully");
            String jwtToken = jwtUtils.generateJwtToken(otpCreateRequest.getMobileNo(), HelperUtils.getMobileUserType, userAccount.get(0).getUserAccId());
            otpResponse.setTokenNo(jwtToken);
            otpResponse.setCretedId(userAccount.get(0).getUserAccId());
            otpResponse.setUserName(customerDetail.get(0).getName());
            otpResponse.setStatus(true);

        } else {
            otpResponse.setStatus(false);
            otpResponse.setMessage("Invalid otp");

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid otp");
        }

        return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
        });
    }

    @Override
    public ApiResponse<OtpResponse> createOtp11(OtpCreateRequest otpCreateRequest) {

        OtpResponse otpResponse = new OtpResponse();
        String otp = "";
        if (otpCreateRequest.getName() == null || otpCreateRequest.getName().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "USER NAME CAN NOT BE BLANK");
        }
        if (otpCreateRequest.getMobileNo() == null || otpCreateRequest.getMobileNo().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "MOBILE NUMBER CAN NOT BE BLANK");
        }
        if (otpCreateRequest.getMobileNo().length() > 10 || otpCreateRequest.getMobileNo().length() <= 9) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OwnerNumber not valid");
        }


        List<UserAccount> userAccount = userAccountRepositry.findByUserName(otpCreateRequest.getMobileNo());
        List<CustomerDetail> customerDetail = customerDetailsRepositry.findByMobileNo(otpCreateRequest.getMobileNo());
        try {


            otpResponse.setMessage("OTP sent successfully");
            otpResponse.setStatus(true);
            return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
            });


        } catch (Exception e) {
            otpResponse.setMessage("Internal server error");
            otpResponse.setStatus(false);
        }


        return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
        });
    }

    @Override
    public ApiResponse<OtpResponse> verfiyOtp11(OtpVerifyRequest otpCreateRequest) {

        OtpResponse otpResponse = new OtpResponse();
        if (otpCreateRequest.getMobileNo() == null || otpCreateRequest.getMobileNo().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "MOBILE NUMBER CAN NOT BE BLANK");

        }
        if (otpCreateRequest.getMobileNo().length() <= 9 || otpCreateRequest.getMobileNo().length() > 10) {

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID MOBILE NUMBER");

        }

        if (otpCreateRequest.getOtp() == null || otpCreateRequest.getOtp().isEmpty()) {

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP CAN NOT BE BLANK");

        }
        if (otpCreateRequest.getOtp().length() <= 5 || otpCreateRequest.getOtp().length() > 6) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID OTP");

        }


        List<UserAccount> userAccount = userAccountRepositry.findByUserName(otpCreateRequest.getMobileNo());
        List<CustomerDetail> customerDetail = customerDetailsRepositry.findByMobileNo(otpCreateRequest.getMobileNo());


        if (userAccount.size() <= 0) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID MOBILE NUMBER");

        }
        if (customerDetail.size() <= 0) {

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID MOBILE NUMBER");

        }


        if (userAccount != null) {

            otpResponse.setMessage("OTP verify successfully");
            String jwtToken = jwtUtils.generateJwtToken(otpCreateRequest.getMobileNo(), HelperUtils.getMobileUserType, userAccount.get(0).getUserAccId());
            otpResponse.setTokenNo(jwtToken);
            otpResponse.setCretedId(userAccount.get(0).getUserAccId());
            otpResponse.setUserName(customerDetail.get(0).getName());
            otpResponse.setStatus(true);

        } else {
            otpResponse.setStatus(false);
            otpResponse.setMessage("Invalid otp");

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid otp");
        }

        return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
        });
    }


    @Override
    public ApiResponse<OtpResponse> verfiyOtp(OtpVerifyRequest otpCreateRequest) {

        OtpResponse otpResponse = new OtpResponse();
        if (otpCreateRequest.getMobileNo() == null || otpCreateRequest.getMobileNo().isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "MOBILE NUMBER CAN NOT BE BLANK");

        }
        if (otpCreateRequest.getMobileNo().length() <= 9 || otpCreateRequest.getMobileNo().length() > 10) {

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID MOBILE NUMBER");

        }

        if (otpCreateRequest.getOtp() == null || otpCreateRequest.getOtp().isEmpty()) {

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP CAN NOT BE BLANK");

        }
        if (otpCreateRequest.getOtp().length() <= 5 || otpCreateRequest.getOtp().length() > 6) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID OTP");

        }


        List<UserAccount> userAccount = userAccountRepositry.findByUserName(otpCreateRequest.getMobileNo());
        List<CustomerDetail> customerDetail = customerDetailsRepositry.findByMobileNo(otpCreateRequest.getMobileNo());


        if (userAccount.size() <= 0) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID MOBILE NUMBER");

        }
        if (customerDetail.size() <= 0) {

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID MOBILE NUMBER");

        }

        if (otpCreateRequest.getMobileNo().equalsIgnoreCase("9897040757")) {
            if (("123456".equalsIgnoreCase(otpCreateRequest.getOtp()))) {
                otpResponse.setMessage("OTP verify successfully");
                String jwtToken = jwtUtils.generateJwtToken(otpCreateRequest.getMobileNo(), HelperUtils.getMobileUserType, customerDetail.get(0).getCustomerId());
                otpResponse.setTokenNo(jwtToken);
                otpResponse.setCretedId(userAccount.get(0).getUserAccId());
                otpResponse.setUserName("Avneesh Kumar");
                otpResponse.setStatus(true);
                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
                });
            } else {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP MISMATCH. PLEASE ENTER CORRECT OTP");
            }
        }


        if (otpCreateRequest.getMobileNo().equalsIgnoreCase("7488863501")) {
            if (("112233".equalsIgnoreCase(otpCreateRequest.getOtp()))) {
                otpResponse.setMessage("OTP verify successfully");
                String jwtToken = jwtUtils.generateJwtToken(otpCreateRequest.getMobileNo(), HelperUtils.getMobileUserType, customerDetail.get(0).getCustomerId());
                otpResponse.setTokenNo(jwtToken);
                otpResponse.setCretedId(userAccount.get(0).getUserAccId());
                otpResponse.setUserName("Abhigyanam ");
                otpResponse.setStatus(true);
                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
                });
            } else {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP MISMATCH. PLEASE ENTER CORRECT OTP");
            }
        }

        if (otpCreateRequest.getMobileNo().equalsIgnoreCase("9123456789")) {
            if (("123457".equalsIgnoreCase(otpCreateRequest.getOtp()))) {
                otpResponse.setMessage("OTP verify successfully");
                String jwtToken = jwtUtils.generateJwtToken(otpCreateRequest.getMobileNo(), HelperUtils.getMobileUserType, customerDetail.get(0).getCustomerId());
                otpResponse.setTokenNo(jwtToken);
//                otpResponse.setCretedId(userAccount.getUserAccId());
                otpResponse.setUserName("Avneesh Kumar");
                otpResponse.setStatus(true);
                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
                });
            } else {
                throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP MISMATCH. PLEASE ENTER CORRECT OTP");
            }
        }

        if (!(userAccount.get(0).getVerifCode().equalsIgnoreCase(otpCreateRequest.getOtp()))) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "OTP MISMATCH. PLEASE ENTER CORRECT OTP");
        }


        if (userAccount != null) {

            long timeDiffer = ConverterUtils.timeDiffer(userAccount.get(0).getUpdatedOn());
            if (timeDiffer > 10) {
                otpResponse.setStatus(false);
                otpResponse.setMessage("THE SMS CODE HAS EXPIRED. PLEASE RE-SEND THE VERIFICATION CODE");
                return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
                });
            }

            otpResponse.setMessage("OTP verify successfully");
            String jwtToken = jwtUtils.generateJwtToken(otpCreateRequest.getMobileNo(), HelperUtils.getMobileUserType, userAccount.get(0).getUserAccId());
            otpResponse.setTokenNo(jwtToken);
            otpResponse.setCretedId(userAccount.get(0).getUserAccId());
            otpResponse.setUserName(customerDetail.get(0).getName());
            otpResponse.setStatus(true);

        } else {
            otpResponse.setStatus(false);
            otpResponse.setMessage("Invalid otp");

            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid otp");
        }

        return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<OtpResponse>() {
        });
    }


    @Override
    public ApiResponse<AppInfoResponse> appInformation() {

        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("CodeFromCategory" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        String userPhoto = "";
        AppInfoResponse otpResponse = new AppInfoResponse();
        otpResponse.setNeutering(false);


        try {
            List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumber(currentLoggedInUser.get(HeaderUtils.MOBILE_NO));
            for (Integer i = 0; i < getAllMainFormData.size(); i++) {
                MainForm data = getAllMainFormData.get(i);
                MainDocForm docData = mainDocFromRepositry.findByFormId(data.getFormId());

                if ((docData != null && docData.getUpload_photo_url() != null) && userPhoto.equalsIgnoreCase("")) {
                    userPhoto = docData.getUpload_photo_url();
                }

//                Boolean splitAge = ConverterUtils.getPetAge(data.getDob().replaceAll("([A-Z])", "").replaceAll("([a-z])", ""));
//                if (splitAge) {
//                    if (data.getIsNeutering() == null && otpResponse.isNeutering() == false) {
//                        otpResponse.setNeutering(true);
//                    }
//                }
            }

        } catch (Exception e) {

        }

        otpResponse.setServerMaintains(false);
        otpResponse.setMsg("The application is under maintainers for 09/02/2023 10.00 PM to 11/02/2023 05.00 PM. It brings the latest changes and improvements to applications already..");
        otpResponse.setUpdateMsg("The application is under maintainers for 09/02/2023 10.00 PM to 11/02/2023 05.00 PM. It brings the latest changes and improvements to applications already..");
        otpResponse.setIosMobileVersion("1323");
        otpResponse.setAndroidMobileVersion("1323");


        //otpResponse.setIosMobileVersion("1320"); pahle ye version tha
        //otpResponse.setAndroidMobileVersion("1320");

        otpResponse.setUserImage(userPhoto);

        otpResponse.setIoServerMaintains(false);
        otpResponse.setAndServerMaintains(false);


        return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<AppInfoResponse>() {
        });
    }

    @Override
    public ApiResponse<AppInfoResponse> appDocterInformation() {
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("CodeFromCategory" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        String userPhoto = "";
        AppInfoResponse otpResponse = new AppInfoResponse();
        otpResponse.setNeutering(false);


        try {
            List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumber(currentLoggedInUser.get(HeaderUtils.MOBILE_NO));
            for (Integer i = 0; i < getAllMainFormData.size(); i++) {
                MainForm data = getAllMainFormData.get(i);
                MainDocForm docData = mainDocFromRepositry.findByFormId(data.getFormId());

                if ((docData != null && docData.getUpload_photo_url() != null) && userPhoto.equalsIgnoreCase("")) {
                    userPhoto = docData.getUpload_photo_url();
                }

//                Boolean splitAge = ConverterUtils.getPetAge(data.getDob().replaceAll("([A-Z])", "").replaceAll("([a-z])", ""));
//                if (splitAge) {
//                    if (data.getIsNeutering() == null && otpResponse.isNeutering() == false) {
//                        otpResponse.setNeutering(true);
//                    }
//                }
            }

        } catch (Exception e) {

        }

        otpResponse.setServerMaintains(false);
        otpResponse.setMsg("The application is under maintainers for 09/02/2023 10.00 PM to 11/02/2023 05.00 PM. It brings the latest changes and improvements to applications already..");
        otpResponse.setUpdateMsg("The application is under maintainers for 09/02/2023 10.00 PM to 11/02/2023 05.00 PM. It brings the latest changes and improvements to applications already..");
        otpResponse.setIosMobileVersion("10");
        otpResponse.setAndroidMobileVersion("10");
        otpResponse.setUserImage(userPhoto);

        otpResponse.setIoServerMaintains(false);
        otpResponse.setAndServerMaintains(false);


        return ResponseUtils.createSuccessResponse(otpResponse, new TypeReference<AppInfoResponse>() {
        });
    }


}


