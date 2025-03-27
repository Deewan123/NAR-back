package com.sdd.service;

import com.sdd.request.OtpCreateRequest;
import com.sdd.request.OtpVerifyRequest;
import com.sdd.response.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NotificationService {

    ResponseEntity<ApiResponse<MainFormResponse>> sendNotification(Note note, String token);

}
