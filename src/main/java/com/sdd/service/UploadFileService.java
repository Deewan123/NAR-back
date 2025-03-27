package com.sdd.service;

import com.sdd.request.OtpCreateRequest;
import com.sdd.request.OtpVerifyRequest;
import com.sdd.response.ApiResponse;
import com.sdd.response.AppInfoResponse;
import com.sdd.response.OtpResponse;
import com.sdd.response.UplaodDocuments;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadFileService {
    ApiResponse<UplaodDocuments> fileUplaod(MultipartFile file) throws IOException;
}
