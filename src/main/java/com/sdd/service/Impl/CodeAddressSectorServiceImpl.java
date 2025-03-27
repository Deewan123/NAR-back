package com.sdd.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sdd.entities.CodeAddressSector;
import com.sdd.entities.repository.CodeAddressSectorRepositry;
import com.sdd.jwt.HeaderUtils;
import com.sdd.jwt.JwtUtils;
import com.sdd.response.ApiResponse;
import com.sdd.service.CodeAddressSectorServices;
import com.sdd.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CodeAddressSectorServiceImpl implements CodeAddressSectorServices {

    @Autowired
    private CodeAddressSectorRepositry codeAddressSectorRepositry;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HeaderUtils headerUtils;

    @Override
    public ApiResponse<List<CodeAddressSector>> getAllSector() {
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("SectorServiceImpl" + currentLoggedInUser.get(HeaderUtils.USER_ID));

        List<CodeAddressSector> sectors = codeAddressSectorRepositry.findAllByOrderByDescrAsc();
        return ResponseUtils.createSuccessResponse(sectors, new TypeReference<List<CodeAddressSector>>() {});

    }
}


