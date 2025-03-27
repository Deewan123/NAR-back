package com.sdd.service.Impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;


public class FirebaseMessagingService {




    public void sendNotification(String title, String body, String token) throws FirebaseMessagingException, IOException {

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("firebase-service-account.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "my-app");
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance(app);
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message
                .builder()
                .setToken(token)            //this can be setTopic() too
                .setNotification(notification)
//                .putAllData(body)
                .build();

        String response =  firebaseMessaging.send(message);

    }

}