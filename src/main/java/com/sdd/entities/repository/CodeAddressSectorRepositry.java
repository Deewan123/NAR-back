package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.CodeAddressSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeAddressSectorRepositry extends JpaRepository<CodeAddressSector,Integer> {


    CodeAddressSector findBySectorId(String sectorId);
    List<CodeAddressSector> findAllByOrderByDescrAsc();
}

