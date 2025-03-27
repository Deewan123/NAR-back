package com.sdd.service;


import com.sdd.entities.CodeBreedType;
import com.sdd.response.ApiResponse;

import java.util.List;

public interface CodeBreedTypeServices {

    ApiResponse<List<CodeBreedType>> geAlltBreedById(String catId);
}
