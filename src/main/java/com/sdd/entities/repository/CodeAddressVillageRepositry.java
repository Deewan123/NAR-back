package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.CodeAddressVillage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeAddressVillageRepositry extends JpaRepository<CodeAddressVillage,Integer> {


    CodeAddressVillage findByVillageId(String villageId);
    List<CodeAddressVillage> findByOrderByDescrAsc();
}

