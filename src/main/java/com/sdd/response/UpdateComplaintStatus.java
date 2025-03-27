package com.sdd.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateComplaintStatus {

    private String userName;
    private String Message;
    private String logMessage;
}
