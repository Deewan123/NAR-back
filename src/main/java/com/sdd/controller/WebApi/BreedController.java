package com.sdd.controller.WebApi;


import com.sdd.entities.CodeBreedType;
import com.sdd.entities.CodeFromCategory;
import com.sdd.response.ApiResponse;
import com.sdd.response.DashboardResponse;
import com.sdd.service.CodeBreedTypeServices;
import com.sdd.service.DashBoardServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/breed")
@Slf4j
public class BreedController {


    @Autowired
    private CodeBreedTypeServices codeBreedTypeServices;


    @GetMapping("/getBreedById/{catId}")
    public ResponseEntity<ApiResponse<List<CodeBreedType>>> getAllBreed(@PathVariable(value = "catId") String catId){
        ApiResponse<List<CodeBreedType>> getAllAddress = codeBreedTypeServices.geAlltBreedById(catId);
        return new ResponseEntity<>(getAllAddress, HttpStatus.OK);
    }

}
