package com.sdd.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sdd.entities.StaffDetails;
import com.sdd.entities.UserAccount;
import com.sdd.entities.UserLoginLog;
import com.sdd.entities.UserPasswordLog;
import com.sdd.entities.repository.StaffDetailsRepositry;
import com.sdd.entities.repository.UserAccountRepositry;
import com.sdd.entities.repository.UserLoginLogRepositry;
import com.sdd.entities.repository.UserPasswordLogRepositry;
import com.sdd.exception.SDDException;
import com.sdd.jwt.HeaderUtils;
import com.sdd.jwt.JwtUtils;
import com.sdd.login.LoginRequest;
import com.sdd.request.BockUserRequest;
import com.sdd.request.ChangePasswordRequest;
import com.sdd.request.CreateUserRequest;
import com.sdd.response.*;
import com.sdd.service.UserAccountServices;
import com.sdd.utils.ConverterUtils;
import com.sdd.utils.HelperUtils;
import com.sdd.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@AllArgsConstructor
public class UserAccountServiceImpl implements UserAccountServices {


    @Autowired
    private UserAccountRepositry userAccountRepositry;

    @Autowired
    private UserLoginLogRepositry userLoginLogRepositry;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private StaffDetailsRepositry staffDetailsRepositry;

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private UserPasswordLogRepositry userPasswordLogRepositry;



    @Override
    public ApiResponse<LoginResponse> login(LoginRequest loginRequest) {
        UserLoginLog userLoginLog = new UserLoginLog();

        if (loginRequest.getUserId() == null || loginRequest.getUserId().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "THAT'S NOT THE RIGHT USERNAME OR PASSWORD. PLEASE TRY AGAIN");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "THAT'S NOT THE RIGHT USERNAME OR PASSWORD. PLEASE TRY AGAIN");
        }
        List<UserAccount> user = userAccountRepositry.findByUserName(loginRequest.getUserId());
        if(user.size() <= 0){
            throw new SDDException(HttpStatus.NOT_FOUND.value(), "THAT'S NOT THE RIGHT USERNAME OR PASSWORD. PLEASE TRY AGAIN");
        }
        String encrypt = ConverterUtils.encrypt("NAPR",loginRequest.getPassword());
        System.out.println("encrypt password:-  " + encrypt);
        if(!(encrypt.equalsIgnoreCase(user.get(0).getPassword()))){
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "THAT'S NOT THE RIGHT USERNAME OR PASSWORD. PLEASE TRY AGAIN");
        }
        if(!(user.get(0).getUserType().equalsIgnoreCase("AD") || user.get(0).getUserType().equalsIgnoreCase("SD")) ){
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "YOU ARE NOT AUTHORIZED FOR LOGIN");
        }
        if((user.get(0).getAccount_expired() == true && user.get(0).getAccount_locked() == true)){
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "YOU ARE NOT AUTHORIZED FOR LOGIN BECAUSE YOUR ACCOUNT IS LOCKED");
        }

        user.get(0).setLogin_count(user.get(0).getLogin_count().add(new BigInteger("1")));
        user.get(0).setLastLoginDt(new Timestamp(new Date().getTime()));
        userAccountRepositry.save(user.get(0));

        userLoginLog.setLogId(HelperUtils.getUserLoginLogId());
        userLoginLog.setCreatedOn(HelperUtils.getCurrentTimeStamp());
        userLoginLog.setIsFlag(1);
        userLoginLog.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        userLoginLog.setIpAddress(InetAddress.getLoopbackAddress().getHostAddress());
        userLoginLog.setIsBlockCount(0);
        userLoginLog.setLogInDate(HelperUtils.getCurrentTimeStamp());
        userLoginLog.setStatus(user.get(0).getUserType());
        userLoginLog.setUserAccId(user.get(0).getUserAccId());
        UserLoginLog userLoginLogSaveData =userLoginLogRepositry.save(userLoginLog);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.get(0).getUserAccId());
        loginResponse.setLogId(userLoginLogSaveData.getLogId());
        loginResponse.setMailId(user.get(0).getMailId());
        loginResponse.setUserType(user.get(0).getUserType());
        loginResponse.setUserName(user.get(0).getUserName());
        loginResponse.setLastLoginDt(user.get(0).getLastLoginDt());
        loginResponse.setSetToken(jwtUtils.generateJwtToken(user.get(0).getUserId(),user.get(0).getUserType(),""));
        return ResponseUtils.createSuccessResponse(loginResponse, new TypeReference<LoginResponse>() {
        });
    }

    @Override
    public ApiResponse<LogoutResponse> createUser(CreateUserRequest createUserRequest) {
        LogoutResponse userCreate = new LogoutResponse();

        if (createUserRequest.getMailId() == null || createUserRequest.getMailId().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "MAIL ID CAN NOT BE BLANK");
        }
        if (createUserRequest.getPassword() == null || createUserRequest.getPassword().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "PASSWORD CAN NOT BE BLANK");
        }
        if (createUserRequest.getUserType() == null || createUserRequest.getUserType().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "USER TYPE CAN NOT BE BLANK");
        }
        if (createUserRequest.getUserName() == null || createUserRequest.getUserName().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "USER NAME CAN NOT BE BLANK");
        }
        List<UserAccount> userAccount= userAccountRepositry.findByUserName(createUserRequest.getMailId());
        if(userAccount.size() < 0){
            if (createUserRequest.getUserName() == null || createUserRequest.getUserName().isEmpty()) {
                throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "USER ALREADY EXIST");
            }
        }


//        if (!(createUserRequest.getUserType().equalsIgnoreCase("AD"))) {
//            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "YOU ARE NOT AUTHORIZED FOR ENTRY");
//        }
        if(createUserRequest.getUserType().equalsIgnoreCase("SD")){
            StaffDetails staffDetails = new StaffDetails();
            staffDetails.setStaff_id(HelperUtils.getUserAccountId());
            staffDetails.setCreatedOn(HelperUtils.getCurrentTimeStamp());
            staffDetails.setIsFlag(1);
            staffDetails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
            staffDetails.setEmailId(createUserRequest.getMailId());
            staffDetails.setMobile(createUserRequest.getMobileNo());
            staffDetails.setName(createUserRequest.getUserName());
            staffDetailsRepositry.save(staffDetails);
        }

        UserAccount  user = new UserAccount();
        user.setLogin_count(new BigInteger("0"));
        user.setCreatedOn(HelperUtils.getCurrentTimeStamp());
        user.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        user.setUserAccId(HelperUtils.getUserAccountAccId());
        user.setIsFlag(1);
        user.setCredentials_expired(true);
        user.setEnabled(true);
        user.setIsFlag(1);
        user.setAccount_locked(true);
        user.setAccount_expired(true);
        user.setUserId(HelperUtils.getUserAccountId());
        user.setPassword(ConverterUtils.encrypt("NAPR",createUserRequest.getPassword()));
        user.setMailId(createUserRequest.getMailId());
        user.setUserType(createUserRequest.getUserType().toUpperCase());
        user.setUserName(createUserRequest.getMailId());
        UserAccount  data = userAccountRepositry.save(user);

        userCreate.setMessage("USER CREATE SUCCESSFULLY");
        return ResponseUtils.createSuccessResponse(userCreate, new TypeReference<LogoutResponse>() {
        });
    }

    @Override
    public ApiResponse<LogoutResponse> blockUser(BockUserRequest bockUserRequest) {
        LogoutResponse logoutResponse = new LogoutResponse();
        if (bockUserRequest.getUserId() == null || bockUserRequest.getUserId().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "USER ID CAN NOT BE BLANK");
        }
        UserAccount user = userAccountRepositry.findByUserAccId(bockUserRequest.getUserId());
        if(user == null){
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "THAT'S NOT THE RIGHT USERNAME OR PASSWORD. PLEASE TRY AGAIN");
        }

        user.setIsBlockTo(HelperUtils.getCurrentTimeStamp());
        user.setEnabled(false);
        user.setAccount_expired(false);
        user.setCredentials_expired(false);
        user.setAccount_locked(false);
        user.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        userAccountRepositry.save(user);

        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setIsBlockTo(HelperUtils.getCurrentTimeStamp());
        userLoginLog.setLogId(HelperUtils.getUserLoginLogId());
        userLoginLog.setCreatedOn(HelperUtils.getCurrentTimeStamp());
        userLoginLog.setIsFlag(1);
        userLoginLog.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        userLoginLog.setIpAddress(InetAddress.getLoopbackAddress().getHostAddress());
        userLoginLog.setIsBlockCount(1);
        userLoginLog.setLogInDate(HelperUtils.getCurrentTimeStamp());
        userLoginLog.setStatus(user.getUserType());
        userLoginLog.setUserAccId(user.getUserAccId());
        UserLoginLog userLoginLogSaveData =userLoginLogRepositry.save(userLoginLog);

        logoutResponse.setLogMessage(userLoginLogSaveData.getLogId());
        logoutResponse.setMessage("USER ACCOUNT LOCKED SUCCESSFULLY");
        userLoginLogRepositry.save(userLoginLog);
        return ResponseUtils.createSuccessResponse(logoutResponse, new TypeReference<LogoutResponse>() {
        });

    }

    @Override
    public ApiResponse<ChangePasswordResponse> changePassword(ChangePasswordRequest changePasswordRequest) {
        ChangePasswordResponse changePasswordResponse = new ChangePasswordResponse();
        if (changePasswordRequest.getUserId() == null || changePasswordRequest.getUserId().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "USER ID CAN NOT BE BLANK");
        }
        if (changePasswordRequest.getNewPassword() == null || changePasswordRequest.getNewPassword().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "NEW PASSWORD CAN NOT BE BLANK");
        }
        if (changePasswordRequest.getOldPassword() == null || changePasswordRequest.getOldPassword().isEmpty()) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OLD PASSWORD CAN NOT BE BLANK");
        }
        if (changePasswordRequest.getOldPassword().equalsIgnoreCase(changePasswordRequest.getNewPassword())) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "OLD PASSWORD AND NEW PASSWORD CAN NOT SAME");
        }
        List<UserAccount> user = userAccountRepositry.findByUserName(changePasswordRequest.getUserId());
        if (user.size() <= 0 ) {
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "INVALID USER ID");
        }
        if(!(user.get(0).getPassword().equalsIgnoreCase(ConverterUtils.encrypt("NAPR",changePasswordRequest.getOldPassword())))){
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "INVALID OLD PASSWORD");
        }

        user.get(0).setPassword(ConverterUtils.encrypt("NAPR",changePasswordRequest.getNewPassword()));
        user.get(0).setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        userAccountRepositry.save(user.get(0));

        UserPasswordLog userPasswordLog = new UserPasswordLog();
        userPasswordLog.setUser_pass_id(HelperUtils.getUserPasswordLogId());
        userPasswordLog.setCreatedOn(HelperUtils.getCurrentTimeStamp());
        userPasswordLog.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
        userPasswordLog.setIsFlag(1);
        userPasswordLog.setPassword(user.get(0).getPassword());
        userPasswordLog.setUser_acc_id(user.get(0).getUserAccId());
        userPasswordLogRepositry.save(userPasswordLog);

        changePasswordResponse.setUserName(user.get(0).getUserName());
        changePasswordResponse.setMessage("PASSWORD UPDATE SUCCESSFULLY");
        return ResponseUtils.createSuccessResponse(changePasswordResponse, new TypeReference<ChangePasswordResponse>() {
        });
    }

    @Override
    public ApiResponse<LogoutResponse> logout(String logId) {
        LogoutResponse logoutResponse = new LogoutResponse();
        UserLoginLog userLoginLog = userLoginLogRepositry.findByLogId(logId);

        if(userLoginLog == null){
            logoutResponse.setLogMessage("INVALID LOD_ID");
            logoutResponse.setMessage("LOGOUT SUCCESSFULLY");
            return ResponseUtils.createSuccessResponse(logoutResponse, new TypeReference<LogoutResponse>() {
            });
        }else{
            userLoginLog.setLogOutDate(HelperUtils.getCurrentTimeStamp());
            logoutResponse.setLogMessage("VALID LOG_ID");
            logoutResponse.setMessage("LOGOUT SUCCESSFULLY");
            userLoginLogRepositry.save(userLoginLog);
            return ResponseUtils.createSuccessResponse(logoutResponse, new TypeReference<LogoutResponse>() {
            });
        }
    }

    @Override
    public ApiResponse<List<AllUserResponse>> getAllUser() {
        List<AllUserResponse> userResponseList = new ArrayList<AllUserResponse>();


        List<UserAccount> userList = userAccountRepositry.findByUserTypeOrUserType("AD","SD");
        if (!CollectionUtils.isEmpty(userList)) {
            userList.forEach(userData -> {
                AllUserResponse userResponseData = new AllUserResponse();
                userResponseData.setUserId(userData.getUserId());
                userResponseData.setAccount_expired(userData.getAccount_expired());
                userResponseData.setAccount_locked(userData.getAccount_locked());
                userResponseData.setEnabled(userData.getEnabled());
                userResponseData.setCreatedOn(userData.getCreatedOn());
                userResponseData.setUserName(userData.getUserName());
                userResponseData.setUserType(userData.getUserType());
                userResponseData.setUserAccId(userData.getUserAccId());
                userResponseData.setCredentials_expired(userData.getCredentials_expired());
                userResponseData.setLastLoginDt(userData.getLastLoginDt());
                userResponseData.setLogin_count(userData.getLogin_count());
                userResponseData.setMailId(userData.getMailId());
                userResponseData.setUpdatedOn(userData.getUpdatedOn());
                userResponseData.setIsBlockTo(userData.getIsBlockTo());
                userResponseList.add(userResponseData);
            });
        }
        return ResponseUtils.createSuccessResponse(userResponseList, new TypeReference<List<AllUserResponse>>() {
        });
    }


}


