package com.sdd.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Map;


@Getter
@Setter
@ToString
@Data
public class Note {
    private String subject;
    private String content;
    private Map<String, String> data;
    private String image;
}