package com.sdd.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sdd.exception.SDDException;
import com.sdd.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class HeaderUtils {

    @Autowired
    private HttpServletRequest httpServletRequest;

    public   static final String SUPER_ADMIN_ROLE_ID ="200";
    public   static final String ADMIN_ROLE_ID ="190";
    public   static final String MANEGER_ROLE_ID ="180";

    public   static final String USER_ID ="user_id";
    public   static final String MOBILE_NO ="mobile_no";
    public   static final String LEVEL ="level";



    public   static final String UNAME ="noauth";
    public   static final String PASSWORD ="121212";
    public   static final String SENDER ="NOIDAS";
    public   static final String TEMPID ="1407162885645962467";
    public   static final String ROUTE ="TA";
    public   static final Integer MSGTYPE = 1;
    public   static final String MSG = "You OTP for NAPR ";


    public   static final String MSGURL = "http://www.bulksms.in.net/index.php/smsapi/httpapi/?uname="+UNAME+"&password="+PASSWORD +
            "&sender="+SENDER+"&tempid="+TEMPID+"&receiver=9897040757&route="+ROUTE+"&msgtype="+MSGTYPE+"&" +
            "sms=Your%20OTP%20for%20NAPR%20is%20{#var#";


//     "/?uname=noauth&password=121212&sender=NOIDAS&tempid=1407162885645962467" +
//             "&receiver=9897040757" +
//             "&route=TA&msgtype=1&" +
//             "sms=Your%20OTP%20for%20NAPR%20is%20"



    public String getTokeFromHeader(){

        String token = httpServletRequest.getHeader("token");
        if(token==null){
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(),"INVALID TOKEN. LOGIN AGAIN");
        }
        return token;
    }



    /**
     * 	public String generateJwtToken(LoginDetails loginDetails) {
     * 		return Jwts.builder()
     * 				.setSubject((loginDetails.getUserId()+": "+loginDetails.getRoleId()+ " : "+ loginDetails.getLevel()))
     * 				.setIssuedAt(new Timestamp())
     * 				.setExpiration(new Timestamp((new Timestamp()).getTime() + jwtExpirationMs))
     * 				.signWith(SignatureAlgorithm.HS512, jwtSecret)
     * 				.compact();
     *        }
     * @param token
     * @return
     */

    public Map<String,String> getUserCurrentDetails(String token){
        String[] arr = token.split(":");
        Map<String,String> loggedInUserDetails = new HashMap<>();
        loggedInUserDetails.put(MOBILE_NO,(arr[0].trim()));
        loggedInUserDetails.put(USER_ID,(arr[1].trim()));
        loggedInUserDetails.put(LEVEL,(arr[2].trim()));
        return Collections.unmodifiableMap(loggedInUserDetails);
    }

    public   static final String NewAPIKEY ="xmo873KMUBrVMosrkx9hcfL5r5VyiERr0DXbKF94Z3Q=";
    public   static final String NewClintId ="aca44878-8958-4943-bf7f-da5d20cc748b";
    public   static final String NewSENDER ="NOAUTH";
    public   static final String NewMSG = "Dear User, Your OTP is {#var#}. DO NOT disclose this OTP to anyone. This is for online use by you only. Regards, http://mynoida.in";


    public   static final String BookVaccination = "Dear Applicant, Your application no {#var#} is scheduled for Vaccination on {#var#}. Please keep your documents ready on   visit.";
    public   static final String completeVaccination = "Dear Applicant, Your application no {#var#} for Vaccination is completed {#var#}. Regards NAPR.";
    public   static final String renewalForm = "Dear Applicant, A kind reminder to pay the pet registration of Rs.{#var#} for name {#var#}.Last date for renewal is {#var#}. Please ignore if already paid.  ";
    public   static final String applicationStatus = "Dear Applicant, Your pet application status  has been changed. Please visit in NAPR app.";
    public   static final String paymentConfirmation = "Dear Applicant, Your Payment fee of Rs. {#var#} has been success for NAPR app. Application no : {#var#} Transaction Id :{#var#}";


}
