package com.sdd.controller.MobileApi;


import com.sdd.entities.CodeFromCategory;
import com.sdd.response.ApiResponse;
import com.sdd.service.CodeFromCategoryServices;
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
@RequestMapping("/petCatMobile")
@Slf4j
public class PetCatMobileController {


    @Autowired
    private CodeFromCategoryServices codeFromCategoryServices;


    @GetMapping("/getAllCategry")
    public ResponseEntity<ApiResponse<List<CodeFromCategory>>> getAllBreed(){
        ApiResponse<List<CodeFromCategory>> getAllAddress = codeFromCategoryServices.getAllSectorMobile();
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }

}
