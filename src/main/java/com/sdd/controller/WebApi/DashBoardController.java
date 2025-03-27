package com.sdd.controller.WebApi;


import com.sdd.response.ApiResponse;
import com.sdd.response.DashboardResponse;
import com.sdd.service.DashBoardServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/dashboard")
@Slf4j
public class DashBoardController {


    @Autowired
    private DashBoardServices dashBoardServices;



    @GetMapping("/getAllDashboardDetails")
    public ResponseEntity<ApiResponse<DashboardResponse>>  getAllUsers(){
        ApiResponse<DashboardResponse> userResponseApiResponse = dashBoardServices.getAllUsers();
        ResponseEntity<ApiResponse<DashboardResponse>> apiResponse = new ResponseEntity<>(userResponseApiResponse, HttpStatus.OK);
        return apiResponse;
    }
//
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest){
//        log.info("[login] user login {}",loginRequest);
//        return new  ResponseEntity<>(userService.login(loginRequest),HttpStatus.OK);
//    }
//
//     @PostMapping("/create")
//     public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserCreateRequest userCreateRequest){
//        ApiResponse<UserResponse> userResponseApiResponse = userService.createUser(userCreateRequest);
//        return new ResponseEntity<>(userResponseApiResponse,HttpStatus.OK);
//     }
//
//
//    @PostMapping("/logot")
//    public ResponseEntity<ApiResponse<LoginResponse>> logout(@RequestBody LoginRequest loginRequest){
//        log.info("[login] user login {}",loginRequest);
//        return new  ResponseEntity<>(userService.login(loginRequest),HttpStatus.OK);
//    }


}
