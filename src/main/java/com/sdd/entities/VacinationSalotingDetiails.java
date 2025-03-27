package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Entity
@Table(name = "vaccination_slotting")
@Getter
@Setter
public class VacinationSalotingDetiails {



    @Id
    @Column(name = "slotting_id")
    private String slottingId;

    @Column(name = "slotting_time")
    private String slottingTime;

    @Column(name = "slotting_status")
    private String slottingStatus = "0";

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;



}
