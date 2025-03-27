package com.sdd.controller.MobileApi;


import com.google.firebase.messaging.FirebaseMessagingException;
import com.sdd.response.*;
import com.sdd.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/notificationController")

public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/send-notification")
    @ResponseBody
    public ResponseEntity<ApiResponse<MainFormResponse>> sendNotification(@RequestBody Note note, @RequestParam String token) throws FirebaseMessagingException {
        return notificationService.sendNotification(note, token);
    }
}
