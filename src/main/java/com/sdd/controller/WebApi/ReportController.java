package com.sdd.controller.WebApi;

import com.itextpdf.text.DocumentException;
import com.sdd.request.EmailRequest;
import com.sdd.request.PaymentRequest;
import com.sdd.response.ApiResponse;
import com.sdd.response.MailResponce;
import com.sdd.service.ReportServices;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {
    @Autowired
    private ReportServices reportServices;

    @PostMapping("/email")
    public ResponseEntity<ApiResponse<List<MailResponce>>>
    emailData(@RequestBody EmailRequest petReq) throws ParseException, DocumentException, JSONException, IOException {
        return new  ResponseEntity<>(reportServices.emailRecept(petReq),HttpStatus.OK);
    }

    @PostMapping("/paymentrecept")
    public ResponseEntity<ApiResponse<MailResponce>>
    recpetData(@RequestBody PaymentRequest petReq) throws ParseException, DocumentException, JSONException, IOException {
        return new  ResponseEntity<>(reportServices.receptData(petReq),HttpStatus.OK);
    }
}
