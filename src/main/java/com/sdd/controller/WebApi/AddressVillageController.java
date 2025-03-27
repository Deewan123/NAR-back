package com.sdd.controller.WebApi;


import com.sdd.entities.CodeAddressVillage;
import com.sdd.response.ApiResponse;
import com.sdd.response.DashboardResponse;
import com.sdd.service.CodeAddressVillageServices;
import com.sdd.service.DashBoardServices;
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
@RequestMapping("/addressVillage")
@Slf4j
public class AddressVillageController {


    @Autowired
    private CodeAddressVillageServices codeAddressVillageServices;

    @GetMapping("/getAllVillage")
    public ResponseEntity<ApiResponse<List<CodeAddressVillage>>>  getAllUsers(){
        ApiResponse<List<CodeAddressVillage>> getAllVillage = codeAddressVillageServices.getAllVillage();
        ResponseEntity<ApiResponse<List<CodeAddressVillage>>> apiResponse = new ResponseEntity<>(getAllVillage, HttpStatus.OK);
        return apiResponse;
    }

}
