package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.UserAccount;
import com.sdd.entities.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  UserLoginLogRepositry extends JpaRepository<UserLoginLog,Integer> {

    UserLoginLog findByLogId(String logId);


}

