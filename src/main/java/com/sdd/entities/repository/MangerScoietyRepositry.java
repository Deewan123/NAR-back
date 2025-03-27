package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.MangerScoiety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MangerScoietyRepositry extends JpaRepository<MangerScoiety,Integer> {


    MangerScoiety findByManageSocietyId(String societyNumber);
    List<MangerScoiety> findByOrderByNameAsc();
}

