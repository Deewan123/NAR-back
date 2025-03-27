package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;import javax.persistence.Table;
import java.sql.Timestamp;


@Entity
@Table(name = "code_address_sector")
@Getter
@Setter
public class CodeAddressSector {



    @Id
    @Column(name = "sector_id")
    private String sectorId;

    @Column(name = "descr")
    private String descr;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}
