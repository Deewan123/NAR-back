package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.StaffDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffDetailsRepositry extends JpaRepository<StaffDetails,Integer> {


}

