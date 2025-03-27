package com.sdd;

import com.sdd.entities.MainDocForm;
import com.sdd.entities.MainForm;
import com.sdd.entities.VacinationSalotingDetiails;
import com.sdd.entities.repository.MainDocFromRepositry;
import com.sdd.entities.repository.MainFromRepositry;
import com.sdd.entities.repository.VacinationSlotingRepositry;
import com.sdd.jwt.HeaderUtils;
import com.sdd.utils.ConverterUtils;
import com.sdd.utils.HelperUtils;
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
public class TestingScheduler {


    @Autowired
    private VacinationSlotingRepositry vacinationSlotingRepositry;


    @Scheduled(cron = "0 */1 * ? * *")
    public void approve() {


//        for (Integer i = 0; i < 28; i++) {
//
//            VacinationSalotingDetiails vacinationSalotingDetiails = new VacinationSalotingDetiails();
//            vacinationSalotingDetiails.setSlottingId((10 +i)+"");
//            vacinationSalotingDetiails.setUpdatedOn(HelperUtils.getCurrentTimeStamp());
//            vacinationSalotingDetiails.setCreatedOn(HelperUtils.getCurrentTimeStamp());
//            vacinationSlotingRepositry.save(vacinationSalotingDetiails);
//
//        }
    }


}
