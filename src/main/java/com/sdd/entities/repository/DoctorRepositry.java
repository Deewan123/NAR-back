package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.DocterDetiails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepositry extends JpaRepository<DocterDetiails,Integer> {


    DocterDetiails findByMobile(String mobileNo);
    DocterDetiails findByDoctorId(String doctorId);
}

