package com.sdd.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ChangePasswordResponse {

    private String userName;
    private String message;
}
