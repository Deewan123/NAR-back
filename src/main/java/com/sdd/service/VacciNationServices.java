package com.sdd.service;


import com.sdd.request.AppointmentCancelRequest;
import com.sdd.request.AppointmentRescduleRequest;
import com.sdd.request.GetVaccinationSlotRequest;
import com.sdd.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VacciNationServices {


    ApiResponse<List<MainFormResponse>> getVacinationData();

    ApiResponse<List<DocterListResponse>> getAllDocterList(DocterListRequest docterListRequest);

    ApiResponse<UplaodMainFormDocumentsResponse> bookVaccinationAppointment(AppointmentRequest appointmentRequest);

    ApiResponse<List<AppointmentResponse>>  getUserAppointment();

    ApiResponse<List<AppointmentResponse>>  getDoctorCompleteAppointment();

    ApiResponse<List<AppointmentResponse>>  getDoctorPendingAppointment();

    ApiResponse<UplaodMainFormDocumentsResponse> cancelAppointmentByDoctor(AppointmentCancelRequest appointmentCancelRequest);

    ApiResponse<UplaodMainFormDocumentsResponse> appointmentCompleteByDoctor(AppointmentCancelRequest appointmentCancelRequest);

    ApiResponse<VaccinationSlotResponse> getVaccinationSlot(GetVaccinationSlotRequest getVaccinationSlotRequest);

    ApiResponse<UplaodMainFormDocumentsResponse> fileUplaod1(MultipartFile pet1,String formId) throws IOException;

    ApiResponse<List<ReSchedulingVaccinationResponse>> getPendingVaccinationData();

    ApiResponse<UplaodMainFormDocumentsResponse> reScheduleVaccinationAppointment(AppointmentRescduleRequest appointmentRequest);
}
