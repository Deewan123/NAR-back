package com.sdd.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sdd.entities.CustomerDetail;
import com.sdd.entities.MainDocForm;
import com.sdd.entities.MainForm;
import com.sdd.entities.UserAccount;
import com.sdd.entities.repository.CustomerDetailsRepositry;
import com.sdd.entities.repository.MainDocFromRepositry;
import com.sdd.entities.repository.MainFromRepositry;
import com.sdd.entities.repository.UserAccountRepositry;
import com.sdd.exception.SDDException;
import com.sdd.jwt.HeaderUtils;
import com.sdd.jwt.JwtUtils;
import com.sdd.request.OtpCreateRequest;
import com.sdd.request.OtpVerifyRequest;
import com.sdd.response.*;
import com.sdd.service.NotificationService;
import com.sdd.service.OtpService;
import com.sdd.utils.ConverterUtils;
import com.sdd.utils.HelperUtils;
import com.sdd.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private UserAccountRepositry userAccountRepositry;

    @Autowired
    private CustomerDetailsRepositry customerDetailsRepositry;

    @Autowired
    private MainFromRepositry mainFromRepositry;

    @Autowired
    private MainDocFromRepositry mainDocFromRepositry;


    @Override
    public ResponseEntity<ApiResponse<MainFormResponse>> sendNotification(Note note, String token) {



        return null;
    }
}


