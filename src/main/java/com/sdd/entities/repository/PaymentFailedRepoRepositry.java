package com.sdd.entities.repository;



import com.sdd.entities.PaymentFailedDetiails;
import com.sdd.entities.PaymentSucessDetiails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentFailedRepoRepositry extends JpaRepository<PaymentFailedDetiails,Integer> {


}

