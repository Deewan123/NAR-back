package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.CustomerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CustomerDetailsRepositry extends JpaRepository<CustomerDetail,Integer> {

//    long countByCreatedOn(Timestamp date);
    long countByCreatedOnGreaterThanAndCreatedOnLessThan(Timestamp starting, Timestamp ending);

    List<CustomerDetail> findByMobileNo(String mobileNo);

    CustomerDetail findByCustomerId(String s);
}

