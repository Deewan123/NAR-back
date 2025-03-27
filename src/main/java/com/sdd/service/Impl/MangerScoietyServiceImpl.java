package com.sdd.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sdd.entities.CodeAddressSector;
import com.sdd.entities.MangerScoiety;
import com.sdd.entities.repository.CodeAddressSectorRepositry;
import com.sdd.entities.repository.MangerScoietyRepositry;
import com.sdd.jwt.HeaderUtils;
import com.sdd.jwt.JwtUtils;
import com.sdd.response.ApiResponse;
import com.sdd.service.MangerScoietyServices;
import com.sdd.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class MangerScoietyServiceImpl implements MangerScoietyServices {


    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private MangerScoietyRepositry mangerScoietyRepositry;



    @Override
    public ApiResponse<List<MangerScoiety>> getAllScoiety() {
        String token = headerUtils.getTokeFromHeader();
        String tokenWithUsername = jwtUtils.getUserNameFromJwtToken(token);
        Map<String, String> currentLoggedInUser = headerUtils.getUserCurrentDetails(tokenWithUsername);
        System.out.println("SectorServiceImpl" + currentLoggedInUser.get(HeaderUtils.USER_ID));

        List<MangerScoiety> societyAllData = mangerScoietyRepositry.findByOrderByNameAsc();
        return ResponseUtils.createSuccessResponse(societyAllData, new TypeReference<List<MangerScoiety>>() {});

    }
}


