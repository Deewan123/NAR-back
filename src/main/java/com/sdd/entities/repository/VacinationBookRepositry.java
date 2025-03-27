package com.sdd.entities.repository;



import com.sdd.entities.BookVacinationAppDetiails;
import com.sdd.entities.DocterDetiails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface VacinationBookRepositry extends JpaRepository<BookVacinationAppDetiails,Integer> {


    List<BookVacinationAppDetiails> findByMobileNumberAndIsComplete(String mobileNo,String isComplete);
    List<BookVacinationAppDetiails> findByMobileNumber(String mobileNo);
    List<BookVacinationAppDetiails> findByFormId(String mobileNo);
    List<BookVacinationAppDetiails> findByFormIdAndIsComplete(String mobileNo,String isComplete);
    BookVacinationAppDetiails findByAppointmentId(String mobileNo);
    List<BookVacinationAppDetiails> findByDoctorIdAndIsComplete(String mobileNo,String isComplete);
    List<BookVacinationAppDetiails> findByDoctorIdAndIsCompleteAndIsCancel(String mobileNo,String isComplete,String isCancel);
    List<BookVacinationAppDetiails> findByDoctorIdAndAppointmentDateGreaterThanAndAppointmentDateLessThan(String mobileNo, Timestamp startDate, Timestamp date);

    List<BookVacinationAppDetiails> findByDoctorId(String docId);
}

