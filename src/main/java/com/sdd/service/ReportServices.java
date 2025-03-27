package com.sdd.service;
import com.itextpdf.text.DocumentException;
import com.sdd.request.EmailRequest;
import com.sdd.request.PaymentRequest;
import com.sdd.response.ApiResponse;
import com.sdd.response.MailResponce;
import org.json.JSONException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ReportServices {


    ApiResponse<List<MailResponce>>emailRecept(EmailRequest payReq) throws ParseException, JSONException, DocumentException, IOException;


    ApiResponse<MailResponce>receptData(PaymentRequest payReq) throws ParseException, JSONException, DocumentException, IOException;





}
