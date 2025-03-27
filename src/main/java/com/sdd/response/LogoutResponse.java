package com.sdd.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;


@Getter
@Setter
@ToString
public class LogoutResponse {

    private String userName;
    private String Message;
    private String logMessage;
}
