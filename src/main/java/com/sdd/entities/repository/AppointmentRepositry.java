package com.sdd.entities.repository;



import com.sdd.entities.BookVacinationAppDetiails;
import com.sdd.entities.DocterDetiails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepositry extends JpaRepository<BookVacinationAppDetiails,Integer> {



}

