package com.sdd;

import com.sdd.entities.MainDocForm;
import com.sdd.entities.MainForm;
import com.sdd.entities.repository.MainDocFromRepositry;
import com.sdd.entities.repository.MainFromRepositry;
import com.sdd.jwt.HeaderUtils;
import com.sdd.utils.ConverterUtils;
import com.sdd.utils.InputData;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
@Configuration
@EnableScheduling
public class AutoSetExpiryScheduler {

    @Autowired
    private MainFromRepositry mainFromRepositry;
    @Autowired
    private MainDocFromRepositry mainDocFromRepositry;


    @Scheduled(cron = "0 */1 * ? * *")
    public void approve() {

        List<MainForm> getAllMainFormData = mainFromRepositry.findByPaymentStatusAndIsUpdate("CO", "0");
//        List<MainForm> getAllMainFormData = mainFromRepositry.findByOwnerNumber("8920658515");

        System.out.println("scheduler running===============" + getAllMainFormData.size());

        getAllMainFormData.parallelStream().forEach(s -> {
            try {
                call(s);

            } catch (Exception e) {
                System.out.println("scheduler running===============" + e.toString());
                e.printStackTrace();
            }
        });

    }

    public void call(MainForm str) throws IOException {

        Timestamp timestamp = str.getCreatedOn();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());
        cal.setTimeInMillis(timestamp.getTime());
        cal.add(Calendar.YEAR, 1);
        Timestamp timestamp11 = new Timestamp(cal.getTime().getTime());

        str.setExpiryDate(timestamp11);
        str.setIsUpdate("1");



        System.out.println("scheduler working===============" + str.getFormId() + " old " + str.getCreatedOn() + " new " + timestamp11);
        mainFromRepositry.save(str);
    }


    public void deleteData(MainForm str) throws IOException {


//        MainDocForm mainDocForm = mainDocFromRepositry.findByFormId(str.getFormId());
//        mainDocFromRepositry.delete(mainDocForm);
//        mainFromRepositry.delete(str);
////

    }
}
