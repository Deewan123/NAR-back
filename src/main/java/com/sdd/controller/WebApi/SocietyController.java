package com.sdd.controller.WebApi;
import com.sdd.entities.MangerScoiety;
import com.sdd.response.ApiResponse;
import com.sdd.service.MangerScoietyServices;
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
@RequestMapping("/society")
@Slf4j
public class SocietyController {


    @Autowired
    private MangerScoietyServices mangerScoietyServices;



    @GetMapping("/getAllScoicty")
    public ResponseEntity<ApiResponse<List<MangerScoiety>>>  getAllUsers(){
        ApiResponse<List<MangerScoiety>> userResponseApiResponse = mangerScoietyServices.getAllScoiety();
        ResponseEntity<ApiResponse<List<MangerScoiety>>> apiResponse = new ResponseEntity<>(userResponseApiResponse, HttpStatus.OK);
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
