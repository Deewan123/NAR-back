//package com.sdd;
//
//import com.sdd.entities.MainForm;
//import com.sdd.entities.PaymentDetails;
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
//public class UpdateAllOrderIdScheduler {
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
//        List<MainForm> getAllPaymentInfo = mainFromRepositry.findByPaymentStatusIsNull();
//        System.out.println(getAllPaymentInfo.size() +"Working ");
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
//    public void call(MainForm mainForm) throws IOException {
//
//        String orderIdNew = mainForm.getOrderId().replaceAll("([/_])", "");
//        System.out.println(mainForm.getOrderId()+"    Data    "  + orderIdNew);
//
//        mainForm.setTxnId(orderIdNew);
//        mainForm.setOrderId(orderIdNew);
//
//        mainFromRepositry.save(mainForm);
//
//
//        List<PaymentDetails> paymentDetails = paymentDetailsRepositry.findByTxnId(mainForm.getOrderId());
//        for (Integer i = 0; i < paymentDetails.size(); i++) {
//            PaymentDetails paymentDetails1 = paymentDetails.get(i);
//
//            String newPAymentId = paymentDetails1.getTxnId().replaceAll("([/_])", "");
//            System.out.println(paymentDetails1.getTxnId() +   "    Paymet   " + newPAymentId);
//
//            paymentDetails1.setTxnId(newPAymentId);
//            paymentDetailsRepositry.save(paymentDetails1);
//
//        }
//    }
//
//}
