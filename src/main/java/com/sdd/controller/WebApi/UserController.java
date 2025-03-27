package com.sdd.controller.WebApi;


import com.sdd.login.LoginRequest;
import com.sdd.request.BockUserRequest;
import com.sdd.request.ChangePasswordRequest;
import com.sdd.request.CreateUserRequest;
import com.sdd.response.*;
import com.sdd.service.UserAccountServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController  {


    @Autowired
    private UserAccountServices userAccountServices;



    @PostMapping("/loginWebApi")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest){
        log.info("[login] user login {}",loginRequest);
        return new  ResponseEntity<>(userAccountServices.login(loginRequest),HttpStatus.OK);
    }


    @PostMapping("/changePasswordWebApi")
    public ResponseEntity<ApiResponse<ChangePasswordResponse>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        log.info("[login] user changePasswordRequest {}",changePasswordRequest);
        return new  ResponseEntity<>(userAccountServices.changePassword(changePasswordRequest),HttpStatus.OK);
    }


    @GetMapping("/logoutWebApi/{logId}")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(@PathVariable(value = "logId") String logId){
        log.info("[login] user logId {}",logId);
        return new  ResponseEntity<>(userAccountServices.logout(logId),HttpStatus.OK);
    }


    @GetMapping("/getAllUserWebApi")
    public ResponseEntity<ApiResponse<List<AllUserResponse>>> getAllUse(){
        log.info("[login] user getAllUse {}","");
        return new  ResponseEntity<>(userAccountServices.getAllUser(),HttpStatus.OK);
    }



    @PostMapping("/createUserWebApi")
    public ResponseEntity<ApiResponse<LogoutResponse>> createUser( @RequestBody CreateUserRequest createUserRequest){
        log.info("[login] user changePascreateUserswordRequest {}",createUserRequest);
        return new  ResponseEntity<>(userAccountServices.createUser(createUserRequest),HttpStatus.OK);
    }



    @PostMapping("/blockUserWebApi")
    public ResponseEntity<ApiResponse<LogoutResponse>> blockUser(@RequestBody BockUserRequest bockUserRequest){
        log.info("[login] user bockUserRequest {}",bockUserRequest);
        return new  ResponseEntity<>(userAccountServices.blockUser(bockUserRequest),HttpStatus.OK);
    }


}
