package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.ContactUs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactUsRepositry extends JpaRepository<ContactUs,Integer> {


}

