package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "manage_society")
@Getter
@Setter
public class MangerScoiety {



    @Id
    @Column(name = "manage_society_id")
    private String manageSocietyId;

    @Column(name = "address")
    private String address;

    @Column(name = "contact_no")
    private String contact_no;

    @Column(name = "name")
    private String name;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}
