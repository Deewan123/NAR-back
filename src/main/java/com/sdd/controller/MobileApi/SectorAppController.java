package com.sdd.controller.MobileApi;


import com.sdd.entities.CodeAddressSector;
import com.sdd.response.ApiResponse;
import com.sdd.service.CodeAddressSectorServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sectorApp")
@Slf4j
public class SectorAppController {


    @Autowired
    private CodeAddressSectorServices codeAddressSectorServices;


    @GetMapping("/getAllSector")
    public ResponseEntity<ApiResponse<List<CodeAddressSector>>> getAllBreed(){
        ApiResponse<List<CodeAddressSector>> getAllAddress = codeAddressSectorServices.getAllSector();
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }



}
