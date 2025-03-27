package com.sdd.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DashboardResponse {


    private long totalCustomerDetails;
    private long todayCustomerDetails;
    private long totalSocitey;
    private long totalForms;
    private long totalSector;
    private long completeForms;
    private long totalComplaint;
    private long rejectForms;

}
