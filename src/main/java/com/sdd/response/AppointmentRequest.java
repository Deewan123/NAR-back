package com.sdd.response;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.Timestamp;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class AppointmentRequest {


    private String doctorId;
    private String formId;
    private String appointmentDate;
    private String appointmentCompleteDate;
    private String mobileNumber;
    private String slottingtime;


    private String appointmentId;

}
