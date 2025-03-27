package com.sdd.response;

import com.sdd.entities.BookVacinationAppDetiails;
import com.sdd.entities.DocterDetiails;
import com.sdd.entities.MainDocForm;
import com.sdd.entities.MainForm;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class AppointmentResponse {

    DocterDetiails docterDetiails;
    BookVacinationAppDetiails getAllDoctorDetails;
    MainForm mainForm;
    MainDocForm mainDocForm;

}
