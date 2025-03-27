package com.sdd.service.Impl;


import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;


public class APNSMessagingService {

    private final AtomicLong counter = new AtomicLong();
    ApnsService service = null;

    public void sendNotification(String body, String title, String token) throws  IOException {

        System.out.println("Sending an iOS push notification…");

         service = APNS.newService()
                .withCert("/Users/User/Documents/Personal_Projects/Api/src/main/resources/Certificates.p12", "YOUR_PASSWORD")
                .withSandboxDestination()
                .build();

        String payload = APNS.newPayload()
                .alertBody(body)
                .alertTitle(title).build();


        System.out.println("payload: " + payload);

        service.push(token, payload);

        System.out.println("The message has been hopefully sent…");

//        return new Notification(counter.incrementAndGet(), String.format(template, title));
    }


}