package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "manage_dog_census")
@Getter
@Setter
public class MangeDogCensus {



    @Id
    @Column(name = "manage_dog_census_id")
    private String manage_dog_census_id;

    @Column(name = "house_no")
    private String house_no;

    @Column(name = "contact_no")
    private String contact_no;

    @Column(name = "name_of_owner")
    private String name_of_owner;

    @Column(name = "manager_detail_id")
    private String manager_detail_id;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}
