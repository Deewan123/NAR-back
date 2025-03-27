package com.sdd.service;


import com.sdd.entities.MangerScoiety;
import com.sdd.response.ApiResponse;

import java.util.List;

public interface MangerScoietyServices {

    ApiResponse<List<MangerScoiety>> getAllScoiety();
}
