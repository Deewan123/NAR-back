package com.sdd.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppInfoResponse {

    private String androidMobileVersion;
    private String userImage;
    private String iosMobileVersion;
    private String msg;
    private String updateMsg;
    private boolean serverMaintains;
    private boolean ioServerMaintains;
    private boolean andServerMaintains;
    private boolean isNeutering;

}
