package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.MainDocForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainDocFromRepositry extends JpaRepository<MainDocForm,Integer> {


    MainDocForm findByFormId(String formId);
    MainDocForm findByDocFormId(String docId);
}

