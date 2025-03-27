package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.PushToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushTokenRepositry extends JpaRepository<PushToken,Integer> {


}

