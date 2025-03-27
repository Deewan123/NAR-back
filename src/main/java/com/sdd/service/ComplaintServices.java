package com.sdd.service;


import com.sdd.entities.Complaint;
import com.sdd.request.CreateComplaintRequest;
import com.sdd.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ComplaintServices {

    ApiResponse<List<GetAllComplaintResponse>> getAllComplaint();
    ApiResponse<List<GetAllComplaintResponse>> getAllPendingComplaint();
    ApiResponse<UpdateComplaintStatus> updateComplaintStatus(String complaintId);
    ApiResponse<List<GetAllComplaintResponse>> getAllCompleteComplaint();
    ApiResponse<List<Complaint>> getComplaint();
    ApiResponse<ComplaintResponse>  createComplaint(CreateComplaintRequest createComplaintRequest);
    ApiResponse<UplaodDocuments> fileUplaod(MultipartFile file1,MultipartFile file2,MultipartFile file3,MultipartFile fil4,String complaintId) throws IOException;

}
