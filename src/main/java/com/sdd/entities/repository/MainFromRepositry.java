package com.sdd.entities.repository;



import com.sdd.entities.AdminDetiails;
import com.sdd.entities.CodeBreedType;
import com.sdd.entities.MainForm;
import com.sdd.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MainFromRepositry extends JpaRepository<MainForm,Integer> {

    long countByapproverState(String state);

    List<MainForm> findByOwnerNumberAndPaymentStatus(String ownerNumber, String paymentType);
    List<MainForm> findByOwnerNumberAndPaymentStatusAndIsRenewed(String ownerNumber, String paymentTyp,Integer isRenewed);
    List<MainForm> findByOwnerNumberAndPaymentStatusAndApproverStateIsNull(String ownerNumber, String paymentType);
    List<MainForm> findByOwnerNumberAndPaymentStatusAndApproverStateIsNullAndApproverRemarkIsNull(String ownerNumber,String paymentType);
    List<MainForm> findByOwnerNumberAndPaymentStatusAndBreedId(String ownerNumber, String paymentType, CodeBreedType breedId);
    List<MainForm> findByPaymentStatusAndNewRegistrationNoIsNullAndApproverStateIsNull( String paymentType);
    List<MainForm> findByPaymentStatus( String paymentType);
    List<MainForm> findByPaymentStatusAndIsUpdate(String paymentType,String isUpdate);
    List<MainForm> findByPaymentStatusAndCreatedOnGreaterThanAndCreatedOnLessThan(String paymentType, Date endDate, Date endDate1);
    List<MainForm> findByOwnerNumberAndPaymentStatusAndApproverIdAndVerifierState(String ownerNumber, String paymentType, String approverId, String veriferStatte);
    List<MainForm> findByOwnerNumber(String mobileNo);
    MainForm findByNewRegistrationNo(String registrationNo);
    MainForm findByOrderId(String orderId);
    List<MainForm> findByOwnerNumberAndPaymentStatusIsNull( String mobileNo);
    List<MainForm> findByOwnerNumberAndPaymentStatusIsNullAndIsPendingList( String mobileNo,String isPendingList);
    List<MainForm> findByOwnerNumberAndPaymentStatusIsNullOrPaymentStatusAndIsPendingList( String mobileNo,String paymentStatus,String isPendingList);
    List<MainForm> findByOwnerNumberAndOrderIdAndPaymentStatusIsNull( String mobileNo, String orderId);
    List<MainForm> findByOrderIdContainingAndPaymentStatusIsNull(String orderId);
    List<MainForm> findByOrderIdContainingOrOrderIdContainingAndPaymentStatusIsNullOrderByCreatedOnDesc(String orderId,String orderIdNew);
    List<MainForm> findByPaymentStatusIsNull();
    MainForm findByFormId(String formId);
    MainForm findMainForsmByFormId(String formId);
}

