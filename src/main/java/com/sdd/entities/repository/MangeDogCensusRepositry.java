package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.MangeDogCensus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MangeDogCensusRepositry extends JpaRepository<MangeDogCensus,Integer> {


}

