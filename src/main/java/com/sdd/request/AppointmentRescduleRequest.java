package com.sdd.request;

import lombok.Getter;
import lombok.Setter;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class AppointmentRescduleRequest {


    private String doctorId;
    private String formId;
    private String appointmentDate;
    private String mobileNumber;
    private String slottingtime;


    private String appointmentId;

}
