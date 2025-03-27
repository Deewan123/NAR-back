package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.DogDetail;
import com.sdd.entities.DogDetailMainForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogDetailsMainFormRepositry extends JpaRepository<DogDetailMainForm,Integer> {


}

