package com.sdd.service;


import com.sdd.entities.CodeAddressVillage;
import com.sdd.response.ApiResponse;

import java.util.List;

public interface CodeAddressVillageServices {
    ApiResponse<List<CodeAddressVillage>>getAllVillage();
}
