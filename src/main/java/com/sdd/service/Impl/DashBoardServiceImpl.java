package com.sdd.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sdd.entities.repository.*;
import com.sdd.response.ApiResponse;
import com.sdd.response.DashboardResponse;
import com.sdd.service.DashBoardServices;
import com.sdd.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;


@Service
@AllArgsConstructor
public class DashBoardServiceImpl implements DashBoardServices {

    @Autowired
    private CustomerDetailsRepositry cutomerDetailsRepositry;

    @Autowired
    private ComplaintRepositry complaintRepositry;

    @Autowired
    private CodeAddressSectorRepositry codeAddressSectorRepositry;

    @Autowired
    private MangerScoietyRepositry mangerScoietyRepositry;

    @Autowired
    private MainFromRepositry mainFromRepositry;



    @Override
    public ApiResponse<DashboardResponse> getAllUsers() {

        Timestamp today = new Timestamp(new Date().getTime());

        System.out.println("date"+today);

        DashboardResponse dashboardResponse = new DashboardResponse();
        dashboardResponse.setTotalCustomerDetails(cutomerDetailsRepositry.count());
        dashboardResponse.setTodayCustomerDetails(cutomerDetailsRepositry.countByCreatedOnGreaterThanAndCreatedOnLessThan(today,today));
        dashboardResponse.setTotalSocitey(mangerScoietyRepositry.count());
        dashboardResponse.setTotalForms(0);
        dashboardResponse.setTotalSector(codeAddressSectorRepositry.count());
        dashboardResponse.setTotalComplaint(complaintRepositry.count());
        dashboardResponse.setCompleteForms(mainFromRepositry.countByapproverState("AP"));
        dashboardResponse.setRejectForms(0);

        return ResponseUtils.createSuccessResponse(dashboardResponse, new TypeReference<DashboardResponse>() {
        });
    }
}


