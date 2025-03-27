package com.sdd.response;

import com.sdd.entities.VacinationSalotingDetiails;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
public class VaccinationSlotResponse {

    private String msg;
    ArrayList<VacinationSalotingDetiails> slotList;
}
