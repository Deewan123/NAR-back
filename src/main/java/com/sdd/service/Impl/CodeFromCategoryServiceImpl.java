package com.sdd.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sdd.entities.CodeAddressVillage;
import com.sdd.entities.CodeFromCategory;
import com.sdd.entities.repository.CodeAddressVillageRepositry;
import com.sdd.entities.repository.CodeFromCategoryRepositry;
import com.sdd.jwt.HeaderUtils;
import com.sdd.jwt.JwtUtils;
import com.sdd.response.ApiResponse;
import com.sdd.service.CodeFromCategoryServices;
import com.sdd.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CodeFromCategoryServiceImpl implements CodeFromCategoryServices {

    @Autowired
    private CodeFromCategoryRepositry codeFromCategoryRepositry;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HeaderUtils headerUtils;



    @Override
    public ApiResponse<List<CodeFromCategory>> getAllSector() {
//        String token = headerUtils.getTokeFromHeader();
//        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
//        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
//        System.out.println("CodeFromCategory" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        List<CodeFromCategory> codeFromCategories = codeFromCategoryRepositry.findAll();
        return ResponseUtils.createSuccessResponse(codeFromCategories, new TypeReference<List<CodeFromCategory>>() {});

    }

    @Override
    public ApiResponse<List<CodeFromCategory>> getAllSectorMobile() {
//        String token = headerUtils.getTokeFromHeader();
//        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
//        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
//        System.out.println("CodeFromCategory" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        List<CodeFromCategory> codeFromCategories = codeFromCategoryRepositry.findAllByOrderByDescrAsc();
        return ResponseUtils.createSuccessResponseWithCred(codeFromCategories, new TypeReference<List<CodeFromCategory>>() {});

    }
}


