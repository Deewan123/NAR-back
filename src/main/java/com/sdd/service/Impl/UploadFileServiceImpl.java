package com.sdd.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sdd.entities.CustomerDetail;
import com.sdd.entities.UserAccount;
import com.sdd.entities.repository.CustomerDetailsRepositry;
import com.sdd.entities.repository.UserAccountRepositry;
import com.sdd.exception.SDDException;
import com.sdd.jwt.HeaderUtils;
import com.sdd.jwt.JwtUtils;
import com.sdd.request.OtpCreateRequest;
import com.sdd.request.OtpVerifyRequest;
import com.sdd.response.ApiResponse;
import com.sdd.response.AppInfoResponse;
import com.sdd.response.OtpResponse;
import com.sdd.response.UplaodDocuments;
import com.sdd.service.OtpService;
import com.sdd.service.UploadFileService;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@Service
@AllArgsConstructor
public class UploadFileServiceImpl implements UploadFileService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private UserAccountRepositry userAccountRepositry;

    @Autowired
    private CustomerDetailsRepositry customerDetailsRepositry;


    @Override
    public ApiResponse<UplaodDocuments> fileUplaod(MultipartFile file) throws IOException {
        String filePath ="data/mcpets/formSupprotDoc/dog/";

//        String token = headerUtils.getTokeFromHeader();
//        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
//        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
//        System.out.println("UserServiceImpl" + currentLoggedInUser.get(HeaderUtils.USER_ID));
//        if (currentLoggedInUser.get(HeaderUtils.USER_ID) == null || currentLoggedInUser.get(HeaderUtils.USER_ID).isEmpty()) {
//            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "Invalid Token.");
//        }
        if (file == null || file.isEmpty()) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "File can not be blank.");
        }

        String fileExtension = ConverterUtils.getFileExtension(file);
        if (fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".pdf") || fileExtension.equalsIgnoreCase(".png") || fileExtension.equalsIgnoreCase(".jpg")) {

        } else {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "File not support.only image or pdf allow");
        }

        String filename = ConverterUtils.getRandomString(file);
        File targetFile = ConverterUtils.getTargetFile(fileExtension, filename,filePath);

        Path path = Paths.get(targetFile.toString());
        InputStream in = new ByteArrayInputStream(file.getBytes());

        try {
            System.out.println(
                    "Number of bytes copied: "
                            + Files.copy(
                            in, path.toAbsolutePath(),
                            StandardCopyOption.REPLACE_EXISTING));
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        UplaodDocuments uplaodDocuments = new UplaodDocuments();
        uplaodDocuments.setPath(filePath + filename + fileExtension);

        return ResponseUtils.createSuccessResponse(uplaodDocuments, new TypeReference<UplaodDocuments>() {
        });
    }
}


