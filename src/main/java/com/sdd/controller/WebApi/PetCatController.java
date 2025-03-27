package com.sdd.controller.WebApi;


import com.sdd.entities.CodeAddressSector;
import com.sdd.entities.CodeFromCategory;
import com.sdd.response.ApiResponse;
import com.sdd.response.DashboardResponse;
import com.sdd.service.CodeFromCategoryServices;
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
@RequestMapping("/petCat")
@Slf4j
public class PetCatController {


    @Autowired
    private CodeFromCategoryServices codeFromCategoryServices;


    @GetMapping("/getAllCategry")
    public ResponseEntity<ApiResponse<List<CodeFromCategory>>> getAllBreed(){
        ApiResponse<List<CodeFromCategory>> getAllAddress = codeFromCategoryServices.getAllSector();
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }

}
