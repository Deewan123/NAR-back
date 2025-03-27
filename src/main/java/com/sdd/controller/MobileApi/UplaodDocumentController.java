package com.sdd.controller.MobileApi;
import com.sdd.response.ApiResponse;
import com.sdd.response.UplaodDocuments;
import com.sdd.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@CrossOrigin
@RestController
@RequestMapping("/uploadDocument")
@Slf4j
public class UplaodDocumentController {

    @Autowired
    private UploadFileService uploadFileService;

    @PostMapping(value = "/uploadFile")
    public ResponseEntity<ApiResponse<UplaodDocuments>> uploadFile(@FormDataParam("file") MultipartFile file) throws IOException {
        log.info("[login] user login {}");
        return new  ResponseEntity<>(uploadFileService.fileUplaod(file),HttpStatus.OK);
    }

    @RequestMapping(value = "/getFilePath")
    public String getfIlePath() throws IOException {
        log.info("[login] user login {}");

        String data = "";
        String filePath  = new File(".").getCanonicalPath() +"/data/mcpets/formSupprotDoc/dog/1643740373303.jpg/";
        String filePath1  = new File(".").getAbsolutePath() + "/data/mcpets/formSupprotDoc/dog/1643740373303.jpg/";

        if(new File( new File(".").getCanonicalPath() + "/data/mcpets/formSupprotDoc/dog/1643740373303.jpg/").exists()){
            data = data +"1" ;
        }

        if(new File( new File(".").getAbsolutePath() + "/data/mcpets/formSupprotDoc/dog/1643740373303.jpg/").exists()){
            data = data +"2" ;
        }
        return filePath+  "   "+ filePath1;
    }





}
