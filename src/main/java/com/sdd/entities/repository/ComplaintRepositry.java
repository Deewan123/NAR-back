package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.Complaint;
import com.sdd.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepositry extends JpaRepository<Complaint,Integer> {

    List<Complaint> findAllByOrderByCreatedOnDesc();
    List<Complaint> findByCompUserIdOrderByCreatedOnDesc(String complaintId);
    Complaint findByComplaintId(String complaintId);
    List<Complaint> findByIsDoneOrderByCreatedOnDesc(int isDone);

}

