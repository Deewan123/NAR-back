package com.sdd.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.sdd.entities.CodeBreedType;
import com.sdd.entities.CodeFromCategory;
import com.sdd.entities.repository.CodeBreedTypeRepositry;
import com.sdd.entities.repository.CodeFromCategoryRepositry;
import com.sdd.jwt.HeaderUtils;
import com.sdd.jwt.JwtUtils;
import com.sdd.response.ApiResponse;
import com.sdd.service.CodeBreedTypeServices;
import com.sdd.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CodeBreedTypeServiceImpl implements CodeBreedTypeServices {
    @Autowired
    private CodeBreedTypeRepositry codeBreedTypeRepositry;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HeaderUtils headerUtils;


    @Override
    public ApiResponse<List<CodeBreedType>> geAlltBreedById(String catId) {

        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("CodeFromCategory" + currentLoggedInUser.get(HeaderUtils.USER_ID));
        List<CodeBreedType> codeBreedTypes = codeBreedTypeRepositry.findByCatIdOrderByDescrAsc(catId);
        return ResponseUtils.createSuccessResponse(codeBreedTypes, new TypeReference<List<CodeBreedType>>() {});

    }
}


