package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.MangerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MangerDetailsRepositry extends JpaRepository<MangerDetails,Integer> {


}

