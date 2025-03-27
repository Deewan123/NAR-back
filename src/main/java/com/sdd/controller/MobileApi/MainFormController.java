package com.sdd.controller.MobileApi;


import com.sdd.entities.MainDocForm;
import com.sdd.entities.MainForm;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/mainForm")
@Slf4j
public class MainFormController {


    @Autowired
    private MainFormServices mainFormServices;

    @PostMapping("/vaccinationPetV2")
    public ResponseEntity<ApiResponse<MainFormResponse>> createUser1(@RequestBody RenewalFormRequest renewalFormRequest) {
        log.info("[login] create mainForm {}", renewalFormRequest);
        return new ResponseEntity<>(mainFormServices.renewalMainForm1(renewalFormRequest), HttpStatus.OK);
    }

    @GetMapping("/getAllPetStatus")
    public ResponseEntity<ApiResponse<List<MainFormResponse>>> getAllPetStatus() {
        ApiResponse<List<MainFormResponse>> getAllAddress = mainFormServices.getAllMainComplete();
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }


    @GetMapping("/getAllPetStatusByFormId/{formId}")
    public ResponseEntity<ApiResponse<MainFormResponse>> getAllPetStatusByFormId(@PathVariable(value = "formId") String formId) {
        ApiResponse<MainFormResponse> getAllAddress = mainFormServices.getAllMainCompleteRenewForm(formId);
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }


    @GetMapping("/getAllPetPendingStatus")
    public ResponseEntity<ApiResponse<List<MainFormResponse>>> getAllPetPendingStatus() {
        ApiResponse<List<MainFormResponse>> getAllAddress = mainFormServices.getAllPetPendingStatus();
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }


    @GetMapping("/getAllPendingForm/{mobileNo}")
    public ResponseEntity<ApiResponse<List<MainFormResponse>>> getAllPendingForm(@PathVariable(value = "mobileNo") String mobileNo) {
        ApiResponse<List<MainFormResponse>> getAllAddress = mainFormServices.getAllPendingForm(mobileNo);
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }



    @GetMapping("/getAllUnpaidFormForm/{mobileNo}")
    public ResponseEntity<ApiResponse<List<MainFormResponse>>> getAllUnpaidFormForm(@PathVariable(value = "mobileNo") String mobileNo) {
        ApiResponse<List<MainFormResponse>> getAllAddress = mainFormServices.getAllUnpaidFormForm(mobileNo);
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }


//    @GetMapping("/updateForm/{formNo}")
//    public ResponseEntity<ApiResponse<CheckAndUpdatePaymentResponse>> updateForm(@PathVariable(value = "formNo") String formNo) {
//        ApiResponse<CheckAndUpdatePaymentResponse> getAllAddress = mainFormServices.updateForm(formNo);
//        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
//    }


//    @GetMapping("/checkAndUpdatePaymentInfo/{paymentInfo}")
//    public ResponseEntity<ApiResponse<CheckAndUpdatePaymentResponse>> checkAndUpdatePaymentInfo(@PathVariable(value = "paymentInfo") String paymentInfo) {
//        ApiResponse<CheckAndUpdatePaymentResponse> getAllAddress = mainFormServices.checkAndUpdatePaymentInfo(paymentInfo);
//        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
//    }
//

    @GetMapping("/getAllApprovePet")
    public ResponseEntity<ApiResponse<List<MainFormResponse>>> getAllApprovePet() {
        ApiResponse<List<MainFormResponse>> getAllAddress = mainFormServices.getAllApprovePet();
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }

    @GetMapping("/getAllPendingPet/{mobileNo}")
    public ResponseEntity<ApiResponse<List<MainFormResponse>>> getAllPendingPet(@PathVariable(value = "mobileNo") String mobileNo) {
        ApiResponse<List<MainFormResponse>> getAllAddress = mainFormServices.getAllPendingPet(mobileNo);
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }

    @GetMapping("/getClosePendingPetRequest/{formId}")
    public ResponseEntity<ApiResponse<List<MainFormResponse>>> getUpdateIsPendingList(@PathVariable(value = "formId") String formId) {
        ApiResponse<List<MainFormResponse>> getAllAddress = mainFormServices.getUpdateIsPendingList(formId);
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }

    @PostMapping("/createMainFormApi")
    public ResponseEntity<ApiResponse<MainFormResponse>> createUser(@RequestBody MainFormRequest mainFormRequest) {
        log.info("[login] create mainForm {}", mainFormRequest);
        return new ResponseEntity<>(mainFormServices.createMainForm(mainFormRequest), HttpStatus.OK);
    }

    @PostMapping("/createMainFormApiV2")
    public ResponseEntity<ApiResponse<MainFormResponse>> createUser1(@RequestBody MainFormRequest mainFormRequest) {
        log.info("[login] create mainForm {}", mainFormRequest);
        return new ResponseEntity<>(mainFormServices.createMainForm1(mainFormRequest), HttpStatus.OK);
    }


    @PostMapping("/updatePaymentInfo")
    public ResponseEntity<ApiResponse<UpdatePaymentResponse>> updatePaymentInfo(@RequestBody UpdatePaymentRequest mainFormRequest) {
        log.info("[login] create mainForm {}", mainFormRequest);
        return new ResponseEntity<>(mainFormServices.updatePaymentInfo(mainFormRequest), HttpStatus.OK);
    }


    @PostMapping("/updateNeutering")
    public ResponseEntity<ApiResponse<UpdatePaymentResponse>> updateNeutering(@RequestBody UpdatePaymentRequest mainFormRequest) {
        log.info("[login] create mainForm {}", mainFormRequest);
        return new ResponseEntity<>(mainFormServices.updateNeutering(mainFormRequest), HttpStatus.OK);
    }


    @GetMapping("/getAllPetNeutering")
    public ResponseEntity<ApiResponse<List<MainFormResponse>>> getAllPetNeutering() {
        ApiResponse<List<MainFormResponse>> getAllAddress = mainFormServices.getAllgetAllPetNeutering();
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }


    @PostMapping("/updatePaymentInfo1")
    public ResponseEntity<ApiResponse<UpdatePaymentResponse>> updatePaymentInfo1(@RequestBody UpdatePaymentRequest mainFormRequest) {
        log.info("[login] create mainForm {}", mainFormRequest);
        return new ResponseEntity<>(mainFormServices.updatePaymentInfo1(mainFormRequest), HttpStatus.OK);
    }


    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @PostMapping("/uploadPhotoRegistrationApi")
    public ResponseEntity<ApiResponse<UplaodMainFormDocumentsResponse>> uploadPhotoRegistrationApi(@FormDataParam("pet1") MultipartFile pet1,
                                                                                                   @FormDataParam("pet2") MultipartFile pet2,
                                                                                                   @FormDataParam("pet3") MultipartFile pet3,
                                                                                                   @FormDataParam("file4") MultipartFile file4,
                                                                                                   @FormDataParam("file5") MultipartFile file5,
                                                                                                   @FormDataParam("file6") MultipartFile file6,
                                                                                                   @FormDataParam("file7") MultipartFile file7,
                                                                                                   @FormDataParam("file8") MultipartFile file8,
                                                                                                   @FormDataParam("formId") String formId) throws IOException {

        return new ResponseEntity<>(mainFormServices.fileUplaod(pet1, pet2, pet3, file4, file5, file6, file7, file8, formId), HttpStatus.OK);
    }


    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @PostMapping("/uploadPhotoRegistrationApV2")
    public ResponseEntity<ApiResponse<UplaodMainFormDocumentsResponse>> uploadPhotoRegistrationApiV2(@FormDataParam("pet1") MultipartFile pet1,
                                                                                                   @FormDataParam("pet2") MultipartFile pet2,
                                                                                                   @FormDataParam("pet3") MultipartFile pet3,
                                                                                                   @FormDataParam("file4") MultipartFile file4,
                                                                                                   @FormDataParam("file5") MultipartFile file5,
                                                                                                   @FormDataParam("file6") MultipartFile file6,
                                                                                                   @FormDataParam("file7") MultipartFile file7,
                                                                                                   @FormDataParam("file8") MultipartFile file8,
                                                                                                   @FormDataParam("formId") String formId) throws IOException {

        return new ResponseEntity<>(mainFormServices.fileUplaod1(pet1, pet2, pet3, file4, file5, file6, file7, file8, formId), HttpStatus.OK);
    }



    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @PostMapping("/uploadPhotoRegistrationWithDocIdV2")
    public ResponseEntity<ApiResponse<UplaodMainFormDocumentsResponse>> uploadPhotoRegistrationApiV12(@FormDataParam("pet1") MultipartFile pet1,
                                                                                                     @FormDataParam("pet2") MultipartFile pet2,
                                                                                                     @FormDataParam("pet3") MultipartFile pet3,
                                                                                                     @FormDataParam("file4") MultipartFile file4,
                                                                                                     @FormDataParam("file5") MultipartFile file5,
                                                                                                     @FormDataParam("file6") MultipartFile file6,
                                                                                                     @FormDataParam("file7") MultipartFile file7,
                                                                                                     @FormDataParam("file8") MultipartFile file8,
                                                                                                      @FormDataParam("formId") String formId,
                                                                                                      @FormDataParam("docId") String docId) throws IOException {

        return new ResponseEntity<>(mainFormServices.fileUplaod1(pet1, pet2, pet3, file4, file5, file6, file7, file8, formId,docId), HttpStatus.OK);
    }


    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @PostMapping("/uploadPhotoV2")
    public ResponseEntity<ApiResponse<MainDocForm>> uploadPhotoSingleApiV2(@FormDataParam("file") MultipartFile file,
                                                                           @FormDataParam("formId") String formId,
                                                                           @FormDataParam("type") String type) throws IOException {

        return new ResponseEntity<>(mainFormServices.fileUplaod1(file,formId,type), HttpStatus.OK);
    }



    @GetMapping("/getVerifyStatus/{registrationNo}")
    public ResponseEntity<ApiResponse<GetRegistrationInfoResponse>> getAllBreed(@PathVariable(value = "registrationNo") String registrationNo) {
        ApiResponse<GetRegistrationInfoResponse> getAllAddress = mainFormServices.getRregistrationNoInfo(registrationNo);
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }


    @PostMapping("/generatePdfFile")
    public ResponseEntity<ApiResponse<MainFormResponse>> generatePdfFile(@RequestBody MainFormRequest mainFormRequest) {
        log.info("[login] create mainForm {}", mainFormRequest);
        return new ResponseEntity<>(mainFormServices.generatePdfFile(mainFormRequest), HttpStatus.OK);
    }


    @PostMapping("/readCsv")
    public ResponseEntity<ApiResponse<MainFormResponse>> readCsv(@RequestBody MainFormRequest mainFormRequest) {
        log.info("[login] create mainForm {}", mainFormRequest);
        return new ResponseEntity<>(mainFormServices.readCsv(mainFormRequest), HttpStatus.OK);
    }



    @GetMapping("/serverCallBack1")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<ApiResponse<CheckAndUpdatePaymentResponse>> serverCallllBack(@PathVariable HashMap<String, Object> paymentInfo) {
        ApiResponse<CheckAndUpdatePaymentResponse> getAllAddress = mainFormServices.getPaymentCallBack(paymentInfo);
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }


    @PostMapping("/serverCallBack")
    public ResponseEntity<ApiResponse<CheckAndUpdatePaymentResponse>> serverCallllBack1(@RequestParam HashMap<String, Object> data) {
        ApiResponse<CheckAndUpdatePaymentResponse> getAllAddress = mainFormServices.getPaymentCallBack(data);
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }



}
