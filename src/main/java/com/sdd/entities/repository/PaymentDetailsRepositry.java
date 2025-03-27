package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.MainDocForm;
import com.sdd.entities.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDetailsRepositry extends JpaRepository<PaymentDetails,Integer> {


    List<PaymentDetails> findByTxnId(String orderId);
    List<PaymentDetails> findByTxnIdContainingOrderByCreatedOnDesc(String orderId);
}

