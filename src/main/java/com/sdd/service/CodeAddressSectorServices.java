package com.sdd.service;


import com.sdd.entities.CodeAddressSector;
import com.sdd.response.ApiResponse;

import java.util.List;

public interface CodeAddressSectorServices {

    ApiResponse<List<CodeAddressSector>> getAllSector();
}
