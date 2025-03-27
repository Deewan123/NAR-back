package com.sdd.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sdd.entities.CodeAddressSector;
import com.sdd.entities.CodeAddressVillage;
import com.sdd.entities.repository.CodeAddressSectorRepositry;
import com.sdd.entities.repository.CodeAddressVillageRepositry;
import com.sdd.jwt.HeaderUtils;
import com.sdd.jwt.JwtUtils;
import com.sdd.response.ApiResponse;
import com.sdd.service.CodeAddressVillageServices;
import com.sdd.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CodeAddressVillageServiceImpl implements CodeAddressVillageServices {
    @Autowired
    private CodeAddressVillageRepositry codeAddressVillageRepositry;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HeaderUtils headerUtils;


    @Override
    public ApiResponse<List<CodeAddressVillage>> getAllVillage() {

            String token = headerUtils.getTokeFromHeader();
            String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
            Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
            System.out.println("CodeAddressVillage" + currentLoggedInUser.get(HeaderUtils.USER_ID));
            List<CodeAddressVillage> codeAddressVillages = codeAddressVillageRepositry.findByOrderByDescrAsc();
            return ResponseUtils.createSuccessResponse(codeAddressVillages, new TypeReference<List<CodeAddressVillage>>() {});

    }
}


