package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "dog_detail")
@Getter
@Setter
public class DogDetail {



    @Id
    @Column(name = "dog_detail_id")
    private String dogDetailId;

    @Column(name = "manage_dog_census_id")
    private String manageDogCensusId;

    @Column(name = "form_id")
    private String formId;

    @Column(name = "breed_id")
    private String breedId;

    @Column(name = "pet_name")
    private String petName;

    @Column(name = "expiry_date")
    private Timestamp expiryDate;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;





}
