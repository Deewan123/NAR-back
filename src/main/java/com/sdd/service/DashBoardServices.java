package com.sdd.service;


import com.sdd.response.ApiResponse;
import com.sdd.response.DashboardResponse;
import java.util.List;

public interface DashBoardServices {

    ApiResponse<DashboardResponse> getAllUsers();
}
