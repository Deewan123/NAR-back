//package com.sdd;
//
//import com.sdd.entities.MainForm;
//import com.sdd.entities.PaymentDetails;
//import com.sdd.entities.PaymentSucessDetiails;
//import com.sdd.entities.repository.MainDocFromRepositry;
//import com.sdd.entities.repository.MainFromRepositry;
//import com.sdd.entities.repository.PaymentDetailsRepositry;
//import com.sdd.entities.repository.PaymentSucessRepoRepositry;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.List;
//
//
//@Service
//@Configuration
//@EnableScheduling
//public class UpdatePaymentScheduler {
//
//    @Autowired
//    private PaymentDetailsRepositry paymentDetailsRepositry;
//
//    @Autowired
//    private MainFromRepositry mainFromRepositry;
//
//    @Autowired
//    private PaymentSucessRepoRepositry paymentSucessRepoRepositry;
//
//
//    @Scheduled(cron = "0 */1 * ? * *")
//    public void approve() {
//
//
//        List<PaymentSucessDetiails> getAllPaymentInfo = paymentSucessRepoRepositry.findByIsChecked("0");
//        System.out.println("Working ");
//
//        getAllPaymentInfo.parallelStream().forEach(s -> {
//            try {
//                call(s);
//
//
//            } catch (Exception e) {
//                System.out.println("scheduler running===============" + e.toString());
//                e.printStackTrace();
//            }
//        });
//
//
//    }
//
//    public void call(PaymentSucessDetiails str) throws IOException {
//
//
//        MainForm getAllMainFormDataCheck = mainFromRepositry.findByFormId(str.getOrderId());
//
//        if (getAllMainFormDataCheck != null) {
//
//
//            getAllMainFormDataCheck.setPaymentStatus("CO");
//            getAllMainFormDataCheck.setTransactionData(str.getResponse());
//            getAllMainFormDataCheck.setTxnId(str.getOrderId());
//            System.out.println("Data " + getAllMainFormDataCheck.getFormId());
//            mainFromRepositry.save(getAllMainFormDataCheck);
//
//
//            str.setIsChecked("1");
//            paymentSucessRepoRepositry.save(str);
//            System.out.println("Update running=========" + getAllMainFormDataCheck.getFormId());
//            return;
//        }
//
//
//        String strNew = str.getOrderId().replaceAll("([A-Z])", "");
//        strNew = strNew.replaceAll("([a-z])", "");
//        strNew = strNew.replaceAll("([/_])", "");
//        String orderIdNew = str.getOrderId().replaceAll("([/_])", "");
//
//        List<MainForm> getAllMainFormData = mainFromRepositry.findByOrderIdContainingOrOrderIdContainingAndPaymentStatusIsNullOrderByCreatedOnDesc(strNew, orderIdNew);
//
//        if (getAllMainFormData.size() > 0) {
//            MainForm mainForm = getAllMainFormData.get(0);
//            mainForm.setPaymentStatus("CO");
//            mainForm.setTransactionData(str.getResponse());
//            mainForm.setTxnId(str.getOrderId());
//            System.out.println("Data " + mainForm.getFormId());
//            mainFromRepositry.save(mainForm);
//
//
//            str.setIsChecked("1");
//            paymentSucessRepoRepositry.save(str);
//            System.out.println("Update running=========" + mainForm.getFormId());
//        } else {
//            List<PaymentDetails> paymentDetails = paymentDetailsRepositry.findByTxnIdContainingOrderByCreatedOnDesc(strNew);
//            if (paymentDetails.size() > 0) {
//                PaymentDetails paymentDetails1 = paymentDetails.get(0);
//                List<MainForm> getData = mainFromRepositry.findByOwnerNumber(paymentDetails1.getPhone());
//
//                if (getData.size() > 0) {
//
//
//                    for (Integer i = 0; i < getData.size(); i++) {
//                        MainForm mainForm = getData.get(i);
//                        if (mainForm.getOrderId().contains(strNew)) {
//                            if (mainForm.getPaymentStatus().equalsIgnoreCase("CO")) {
//                                str.setIsChecked("1");
//                                paymentSucessRepoRepositry.save(str);
//                                return;
//                            }
//
//                            System.out.println("Data " + mainForm.getOrderId());
//                            mainForm.setPaymentStatus("CO");
//                            mainForm.setTransactionData(str.getResponse());
//                            mainForm.setTxnId(str.getOrderId());
//                            System.out.println("Data " + mainForm.getFormId());
//                            mainFromRepositry.save(mainForm);
//                            System.out.println("Update running=========" + mainForm.getFormId());
//                        }
//                    }
//                }
//            }
//        }
//
//
//    }
//}
