package com.sdd.response;

import com.sdd.entities.BookVacinationAppDetiails;
import com.sdd.entities.VacinationSalotingDetiails;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
public class ReSchedulingVaccinationResponse {

    private String msg;
    BookVacinationAppDetiails bookVacinationAppDetiails;
}
