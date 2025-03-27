package com.sdd.service;


import com.sdd.entities.MainDocForm;
import com.sdd.entities.MainForm;
import com.sdd.request.*;
import com.sdd.response.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface MainFormServices {

    ApiResponse<List<MainFormResponse>> getAllMainComplete();
    ApiResponse<MainFormResponse> getAllMainCompleteRenewForm(String formId);
    ApiResponse<List<MainFormResponse>> getAllPetPendingStatus();

    ApiResponse<GetRegistrationInfoResponse> getRregistrationNoInfo(String registrationNo);

    ApiResponse<MainFormResponse> createMainForm(MainFormRequest mainFormRequest);
    ApiResponse<MainFormResponse> createMainForm1(MainFormRequest mainFormRequest);

    ApiResponse<UplaodMainFormDocumentsResponse> fileUplaod(MultipartFile pet1, MultipartFile pet2, MultipartFile pet3, MultipartFile file4, MultipartFile file5, MultipartFile file6, MultipartFile file7, MultipartFile file8, String formId) throws IOException;
    ApiResponse<UplaodMainFormDocumentsResponse> fileUplaod1(MultipartFile pet1, MultipartFile pet2, MultipartFile pet3, MultipartFile file4, MultipartFile file5, MultipartFile file6, MultipartFile file7, MultipartFile file8, String formId) throws IOException;
    ApiResponse<UplaodMainFormDocumentsResponse> fileUplaod1(MultipartFile pet1, MultipartFile pet2, MultipartFile pet3, MultipartFile file4, MultipartFile file5, MultipartFile file6, MultipartFile file7, MultipartFile file8, String formId, String docId) throws IOException;

    ApiResponse<UpdatePaymentResponse> updatePaymentInfo(UpdatePaymentRequest mainFormRequest);
    ApiResponse<UpdatePaymentResponse> updatePaymentInfo1(UpdatePaymentRequest mainFormRequest);

    ApiResponse<MainFormResponse>  generatePdfFile(MainFormRequest mainFormRequest);
    ApiResponse<MainFormResponse>  readCsv(MainFormRequest mainFormRequest);

    ApiResponse<List<MainFormResponse>> getAllApprovePet();

    ApiResponse<List<MainFormResponse>> getAllPendingForm(String mobileNo);
    ApiResponse<List<MainFormResponse>> getAllUnpaidFormForm(String mobileNo);
    ApiResponse<CheckAndUpdatePaymentResponse> getPaymentCallBack(HashMap<String,Object> paymentInfo);
    ApiResponse<CheckAndUpdatePaymentResponse> getPaymentCallBack1(String paymentInfo);

    ApiResponse<List<MainFormResponse>> getAllPendingPet(String mobileNo);

    ApiResponse<List<MainFormResponse>> getUpdateIsPendingList(String formId);

    ApiResponse<MainDocForm> fileUplaod1(MultipartFile file, String formId, String type);

    ApiResponse<UpdatePaymentResponse>  updateNeutering(UpdatePaymentRequest mainFormRequest);

    ApiResponse<List<MainFormResponse>> getAllgetAllPetNeutering();

    ApiResponse<List<MainFormResponse>> getRenewalData();

    ApiResponse<MainFormResponse>  renewalMainForm1(RenewalFormRequest mainFormRequest);

    ApiResponse<UplaodMainFormDocumentsResponse> fileUplaodRenewal(MultipartFile pet1, MultipartFile pet2, MultipartFile pet3, MultipartFile file7, MultipartFile file8, String formId, String docId);


}
