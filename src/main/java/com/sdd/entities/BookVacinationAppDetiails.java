package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Entity
@Table(name = "vaccination_book")
@Getter
@Setter
public class BookVacinationAppDetiails {



    @Id
    @Column(name = "appointment_id")
    private String appointmentId;

    @Column(name = "doctor_id")
    private String doctorId;

    @Column(name = "form_id")
    private String formId;

    @Column(name = "appointment_date")
    private Timestamp appointmentDate;

    @Column(name = "complete_date")
    private Timestamp appointmentCompleteDate;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_cancel")
    private String isCancel;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "is_complete")
    private String isComplete;


    @Column(name = "mobile_number")
    private String mobileNumber;


    @Column(name = "slotting_time")
    private String slottingTime;

    @Column(name = "user_code")
    private String userCode;

}
