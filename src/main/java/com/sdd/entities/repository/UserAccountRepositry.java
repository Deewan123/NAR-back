package com.sdd.entities.repository;




import com.sdd.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountRepositry extends JpaRepository<UserAccount,Integer> {


//    UserAccount findByUserName(String mail);
    List<UserAccount> findByUserName(String mobileNo);
    UserAccount findByIsBlockTo(String isBlockTo);
    UserAccount findByUserAccId(String mail);
    List<UserAccount> findByUserTypeOrUserType(String userType,String userType1);



}

