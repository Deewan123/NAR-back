package com.sdd.service;


import com.sdd.entities.CodeFromCategory;
import com.sdd.response.ApiResponse;

import java.util.List;

public interface CodeFromCategoryServices {

    ApiResponse<List<CodeFromCategory>> getAllSector();

    ApiResponse<List<CodeFromCategory>> getAllSectorMobile();
}
