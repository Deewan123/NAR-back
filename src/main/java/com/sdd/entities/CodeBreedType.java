package com.sdd.entities;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;


@Entity
@Table(name = "code_breed_type")
@Getter
@Setter
public class CodeBreedType {



    @Id
    @Column(name = "breed_id")
    private String breedId;

    @Column(name = "cat_id")
    private String catId;

    @Column(name = "descr")
    private String descr;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "is_flag")
    private Integer isFlag;


}
