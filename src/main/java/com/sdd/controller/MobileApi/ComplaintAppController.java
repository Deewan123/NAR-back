package com.sdd.controller.MobileApi;


import com.sdd.entities.Complaint;
import com.sdd.request.CreateComplaintRequest;
import com.sdd.response.*;
import com.sdd.service.ComplaintServices;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/complaintApp")
@Slf4j
public class ComplaintAppController {

    @Autowired
    private ComplaintServices complaintServices;


    @GetMapping("/getAllComplaintWebApi")
    public ResponseEntity<ApiResponse<List<GetAllComplaintResponse>>> getAllComplaint(){
        log.info("[Complaint] complaint getAllComplaint {}");
        return new  ResponseEntity<>(complaintServices.getAllComplaint(),HttpStatus.OK);
    }

    @GetMapping("/getComplaintWebApi")
    public ResponseEntity<ApiResponse<List<Complaint>>> getComplaint(){
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


    @PostMapping("/uploadPhotoWebApi")
    public ResponseEntity<ApiResponse<UplaodDocuments>> uploadPhotoWebApi(@FormDataParam("file") MultipartFile file1, @FormDataParam("file") MultipartFile file2, @FormDataParam("file") MultipartFile file3, @FormDataParam("file") MultipartFile file4,@FormDataParam("complaintId") String complaintId) throws IOException {

        return new  ResponseEntity<>(complaintServices.fileUplaod(file1,file2,file3,file4,complaintId),HttpStatus.OK);
    }


}
