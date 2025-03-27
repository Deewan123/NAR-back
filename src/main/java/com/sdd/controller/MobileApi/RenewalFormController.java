package com.sdd.controller.MobileApi;


import com.sdd.entities.MainDocForm;
import com.sdd.request.MainFormRequest;
import com.sdd.request.RenewalFormRequest;
import com.sdd.request.UpdatePaymentRequest;
import com.sdd.response.*;
import com.sdd.service.MainFormServices;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/renewalForm")
@Slf4j
public class RenewalFormController {


    @Autowired
    private MainFormServices mainFormServices;



    @GetMapping("/getRenewalData")
    public ResponseEntity<ApiResponse<List<MainFormResponse>>> getRenewalData() {
        ApiResponse<List<MainFormResponse>> getAllAddress = mainFormServices.getRenewalData();
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }



    @PostMapping("/renewalMainFormApiV2")
    public ResponseEntity<ApiResponse<MainFormResponse>> createUser1(@RequestBody RenewalFormRequest renewalFormRequest) {
        log.info("[login] create mainForm {}", renewalFormRequest);
        return new ResponseEntity<>(mainFormServices.renewalMainForm1(renewalFormRequest), HttpStatus.OK);
    }


    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @PostMapping("/uploadPhotoRenewalApV2")
    public ResponseEntity<ApiResponse<UplaodMainFormDocumentsResponse>> uploadPhotoRegistrationApiV2(@FormDataParam("pet1") MultipartFile pet1,
                                                                                                     @FormDataParam("pet2") MultipartFile pet2,
                                                                                                     @FormDataParam("pet3") MultipartFile pet3,
                                                                                                     @FormDataParam("file7") MultipartFile file7,
                                                                                                     @FormDataParam("file8") MultipartFile file8,
                                                                                                     @FormDataParam("formId") String formId,
                                                                                                     @FormDataParam("docId") String docId) throws IOException {

        return new ResponseEntity<>(mainFormServices.fileUplaodRenewal(pet1, pet2, pet3, file7, file8, formId,docId), HttpStatus.OK);
    }


}
