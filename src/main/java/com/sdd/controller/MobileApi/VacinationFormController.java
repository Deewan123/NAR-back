package com.sdd.controller.MobileApi;


import com.sdd.request.AppointmentCancelRequest;
import com.sdd.request.AppointmentRescduleRequest;
import com.sdd.request.GetVaccinationSlotRequest;
import com.sdd.request.RenewalFormRequest;
import com.sdd.response.*;
import com.sdd.service.MainFormServices;
import com.sdd.service.VacciNationServices;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/vaccinationForm")
@Slf4j
public class VacinationFormController {

    @Autowired
    private VacciNationServices vacciNationServices;


    @GetMapping("/getVaccinationData")
    public ResponseEntity<ApiResponse<List<MainFormResponse>>> getRenewalData() {
        ApiResponse<List<MainFormResponse>> getAllAddress = vacciNationServices.getVacinationData();
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }



    @GetMapping("/getPendingVaccinationData")
    public ResponseEntity<ApiResponse<List<ReSchedulingVaccinationResponse>>> getPendingVaccinationData() {
        ApiResponse<List<ReSchedulingVaccinationResponse>> getAllAddress = vacciNationServices.getPendingVaccinationData();
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }



    @PostMapping("/getAllDoctorList")
    public ResponseEntity<ApiResponse<List<DocterListResponse>>> getAllDoctorList(@RequestBody DocterListRequest docterListRequest) {
        log.info("[login] create mainForm {}", docterListRequest);
        return new ResponseEntity<>(vacciNationServices.getAllDocterList(docterListRequest), HttpStatus.OK);
    }

    @PostMapping("/bookVaccinationAppointment")
    public ResponseEntity<ApiResponse<UplaodMainFormDocumentsResponse>> bookVaccinationAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        log.info("[login] create mainForm {}", appointmentRequest);
        return new ResponseEntity<>(vacciNationServices.bookVaccinationAppointment(appointmentRequest), HttpStatus.OK);
    }

    @GetMapping("/getDoctorCompleteAppointment")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getDoctorCompleteAppointment() {
        return new ResponseEntity<>(vacciNationServices.getDoctorCompleteAppointment(), HttpStatus.OK);
    }

    @GetMapping("/getDoctorPendingAppointment")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getDoctorPendingAppointment() {
        return new ResponseEntity<>(vacciNationServices.getDoctorPendingAppointment(), HttpStatus.OK);
    }

    @GetMapping("/getUserAppointment")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getUserAppointment() {
        return new ResponseEntity<>(vacciNationServices.getUserAppointment(), HttpStatus.OK);
    }

    @PostMapping("/cancelAppointmentByDoctor")
    public ResponseEntity<ApiResponse<UplaodMainFormDocumentsResponse>> cancelAppointmentByDoctor(@RequestBody AppointmentCancelRequest appointmentCancelRequest) {
        ApiResponse<UplaodMainFormDocumentsResponse> getAllAddress = vacciNationServices.cancelAppointmentByDoctor(appointmentCancelRequest);
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }

    @PostMapping("/AppointmentCompleteByDoctor")
    public ResponseEntity<ApiResponse<UplaodMainFormDocumentsResponse>> appointmentCompleteByDoctor(@RequestBody AppointmentCancelRequest appointmentCancelRequest) {
        ApiResponse<UplaodMainFormDocumentsResponse> getAllAddress = vacciNationServices.appointmentCompleteByDoctor(appointmentCancelRequest);
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }






    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @PostMapping("/uploadVaccinationBook")
    public ResponseEntity<ApiResponse<UplaodMainFormDocumentsResponse>> uploadPhotoRegistrationApiV2(@FormDataParam("pet1") MultipartFile pet1, @FormDataParam("formId") String formId) throws IOException {
        return new ResponseEntity<>(vacciNationServices.fileUplaod1(pet1, formId), HttpStatus.OK);
    }


    @PostMapping("/getVaccinationSlot")
    public ResponseEntity<ApiResponse<VaccinationSlotResponse>> getVaccinationSlot(@RequestBody GetVaccinationSlotRequest getVaccinationSlotRequest) {
        ApiResponse<VaccinationSlotResponse> getAllAddress = vacciNationServices.getVaccinationSlot(getVaccinationSlotRequest);
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }


    @PostMapping("/reScheduleVaccinationAppointment")
    public ResponseEntity<ApiResponse<UplaodMainFormDocumentsResponse>> reScheduleVaccinationAppointment(@RequestBody AppointmentRescduleRequest appointmentRequest) {
        log.info("[login] create mainForm {}", appointmentRequest);
        return new ResponseEntity<>(vacciNationServices.reScheduleVaccinationAppointment(appointmentRequest), HttpStatus.OK);
    }

}
