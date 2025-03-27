package com.sdd.controller.WebApi;


import com.sdd.entities.Complaint;
import com.sdd.login.LoginRequest;
import com.sdd.request.BockUserRequest;
import com.sdd.request.ChangePasswordRequest;
import com.sdd.request.CreateComplaintRequest;
import com.sdd.request.CreateUserRequest;
import com.sdd.response.*;
import com.sdd.service.ComplaintServices;
import com.sdd.service.UserAccountServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/complaint")
@Slf4j
public class ComplaintController {

    @Autowired
    private ComplaintServices complaintServices;


    @GetMapping("/getAllComplaintWebApi")
    public ResponseEntity<ApiResponse<List<GetAllComplaintResponse>>> getAllComplaint(){
        log.info("[Complaint] complaint getAllComplaint {}");
        return new  ResponseEntity<>(complaintServices.getAllComplaint(),HttpStatus.OK);
    }

    @GetMapping("/getComplaintWebApi/{userId}")
    public ResponseEntity<ApiResponse<List<Complaint>>> getComplaint(){
//        log.info("[Complaint] complaint complaintId {}",);
        return new  ResponseEntity<>(complaintServices.getComplaint(),HttpStatus.OK);
    }

    @GetMapping("/getAllPendingWebApi")
    public ResponseEntity<ApiResponse<List<GetAllComplaintResponse>>> getAllPendingComplaint(){
        log.info("[Complaint] complaint getAllCompleteComplaint {}");
        return new  ResponseEntity<>(complaintServices.getAllPendingComplaint(),HttpStatus.OK);
    }

    @GetMapping("/getAllCompleteWebApi")
    public ResponseEntity<ApiResponse<List<GetAllComplaintResponse>>> getAllCompleteComplaint(){
        log.info("[Complaint] complaint getAllCompleteComplaint {}");
        return new  ResponseEntity<>(complaintServices.getAllPendingComplaint(),HttpStatus.OK);
    }


    @GetMapping("/updateComplaintStatusWebApi")
    public ResponseEntity<ApiResponse<UpdateComplaintStatus>> updateComplaintStatus(@PathVariable(value = "complaintId") String complaintId){
        log.info("[Complaint] complaint updateComplaintStatus {} "+ complaintId);
        return new  ResponseEntity<>(complaintServices.updateComplaintStatus(complaintId),HttpStatus.OK);
    }

    @PostMapping("/createComplaintWebApi")
    public ResponseEntity<ApiResponse<ComplaintResponse>> createUser( @RequestBody CreateComplaintRequest createComplaintRequest){
        log.info("[login] complaint changePascreateUserswordRequest {}",createComplaintRequest);
        return new  ResponseEntity<>(complaintServices.createComplaint(createComplaintRequest),HttpStatus.OK);
    }
}
