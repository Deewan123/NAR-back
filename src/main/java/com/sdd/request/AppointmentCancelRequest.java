package com.sdd.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppointmentCancelRequest {




    private String appointmentId;
    private String cancelReason;
    private String userCode;

    private String formId;
    private String vaccinationDate;
    private String nextVaccinationDate;


}
