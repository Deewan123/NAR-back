package com.sdd.entities.repository;



import com.sdd.entities.PaymentCallDetiails;
import com.sdd.entities.VacinationSalotingDetiails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacinationSlotingRepositry extends JpaRepository<VacinationSalotingDetiails,Integer> {


}

