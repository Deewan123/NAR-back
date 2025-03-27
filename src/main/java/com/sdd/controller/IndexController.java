package com.sdd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class IndexController {

    private static String applicationName = "Well Come to NAPR 7";
//This build live for mobile application 08/08/2023 ,10 AM
    @GetMapping("/")
    public String login() {
        return applicationName;
    }



}
