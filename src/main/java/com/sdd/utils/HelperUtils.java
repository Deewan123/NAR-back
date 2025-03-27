package com.sdd.utils;


import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class HelperUtils {
    //Development
   // public static String UPLOADFILEPATH = "C://Program Files/Apache Software Foundation/Tomcat 9.0/webapps/data/";
    //public static String DOWNLOADFILE = "http://15.206.123.29:8081/data/";

    //Live
   // public static String UPLOADFILEPATH = "/usr/local/tomcat/data/";
    public static String UPLOADFILEPATH = "/usr/local/tomcat/webapps/data/";
//mcpets
    public static String DOWNLOADFILE = "http://petregistration.mynoida.co.in/data/";

    public static String getMobileUserType = "CD";
    public static Integer getUserIsFlag() {
        return 1;
    }
    public static String getUserLoginLogId() {
        return "ULL"+ ConverterUtils.getRandomTimeStamp();
    }
    public static String getFormId() {return "FMID"+ ConverterUtils.getRandomTimeStamp();}
    public static String getDogId() {
        return "DOGID"+ ConverterUtils.getRandomTimeStamp();
    }
    public static String getDocumentId() {
        return "DOCID"+ ConverterUtils.getRandomTimeStamp();
    }

    public static String getPaymentId() {return "PAYID"+ ConverterUtils.getRandomTimeStamp();}
    public static String getUserPasswordLogId() {
        return "UPL"+ ConverterUtils.getRandomTimeStamp();
    }

    public static String getUserAccountId() {
        return "CSID"+ ConverterUtils.getRandomTimeStamp();
    }
    public static String getBooKingId() {
        return "BKID"+ ConverterUtils.getRandomTimeStamp();
    }
    public static String getComplaintId() {
        return "MD"+ System.currentTimeMillis();
    }

    public static String getUserAccountAccId() {
        return  ""+ConverterUtils.getRandomTimeStamp();
    }

    public static Timestamp getCurrentTimeStamp() {
        return  new Timestamp(new Date().getTime());
    }

    public static String getOrderId() {return ""+ ConverterUtils.getRandomTimeStamp();}

}
