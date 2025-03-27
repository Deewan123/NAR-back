package com.sdd.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmailRequest {
    private String email;
    private String url;
}
