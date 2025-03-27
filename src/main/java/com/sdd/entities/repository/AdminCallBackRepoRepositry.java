package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.PaymentCallDetiails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminCallBackRepoRepositry extends JpaRepository<PaymentCallDetiails,Integer> {


}

