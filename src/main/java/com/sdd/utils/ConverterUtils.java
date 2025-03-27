package com.sdd.utils;


import com.sdd.exception.SDDException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConverterUtils {

    public static Boolean getPetAge(String age) {

        try {
            age = age.trim();
            String[] splitAge = age.trim().split("  ");

            String year = splitAge[0];
            String month = splitAge[1];

            if (Integer.parseInt(year) >= 1) {
                return true;
            }

            if (Integer.parseInt(month) > 6) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;

    }


    public static String checkGetIsvalidOrNor(String dt) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID DATE FORMAT");
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        return sdf1.format(c.getTime());
    }


    public static String getRandomString(MultipartFile file) {
        try {
            Thread.sleep(10);
            return System.currentTimeMillis() + "";
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return null + "";
    }

    public static String getRandomString(String fileName) {
        try {
            Thread.sleep(10);
            return fileName + System.currentTimeMillis() + "";
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return fileName + "";
    }


    public static String getRandomTimeStamp() {
        try {
            Thread.sleep(10);
            return System.currentTimeMillis() + "";
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return null + "";
    }


    public static long timeDiffer(Timestamp dateStart) {

        String dateStop = getCurrentDateWithTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = new Date(dateStart.getTime());
            d2 = format.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = d1.getTime() - d2.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        System.out.println("Time in seconds: " + diffSeconds + " seconds.");
        System.out.println("Time in minutes: " + diffMinutes + " minutes.");
        System.out.println("Time in hours: " + diffHours + " hours.");
        long diffday = diff / (60 * 60 * 1000 * 24);
        return diffday;

    }


    public static long dayDiffer(String returnDate) {
        try {
            String dateStop = getCurrentDateWithTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date d1 = null;
            Date d2 = null;
            try {
                d1 = format.parse(returnDate);
                d2 = format.parse(dateStop);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long diff = d1.getTime() - d2.getTime();
            long diffSeconds = diff / 1000;
            long diffMinutes = diff / (60 * 1000);
            long diffHours = diff / (60 * 60 * 1000);
            long diffday = diff / (60 * 60 * 1000 * 24);
            System.out.println("Time in seconds: " + diffSeconds + " seconds.");
            System.out.println("Time in minutes: " + diffMinutes + " minutes.");
            System.out.println("Time in hours: " + diffHours + " hours.");
            return diffday;
        } catch (Exception e) {
            throw new SDDException(HttpStatus.BAD_REQUEST.value(), "INVALID DATE FORMAT");
        }

    }

    public static long timeDiffer(Timestamp dateStart, Timestamp enddate) {


        Date d1 = null;
        Date d2 = null;
        try {
            d1 = new Date(dateStart.getTime());
            d2 = new Date(enddate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        long diff = d2.getTime() - d1.getTime();

        long diffDays = diff / (60 * 60 * 1000 * 24);

        return diffDays;

    }

    public static File getTargetFile(String fileExtn, String fileName, String fileDestPath) {

        String dirPath = null;
        try {
            dirPath = "/usr/local/tomcat/webapps/" + fileDestPath;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Path path = Paths.get(dirPath);
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException ie) {
                System.out.println("Problem creating directory " + ie);
            }
        }
        File targetFile = new File(path.toString() + "/" + fileName + fileExtn);
        return targetFile;
    }

    public static File getComplaintPathOnly(String fileExtn, String fileName, String fileDestPath) {
        Path path = Paths.get(fileDestPath);
        File targetFile = new File(path.toString() + "/" + fileName + fileExtn);
        return targetFile;
    }


    public static String getFileExtension(MultipartFile file) {
        String fileExtention = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        return fileExtention;
    }

    public static String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return (dtf.format(now));
    }

    public static String getCurrentDateWithFreshTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        return (dtf.format(now) + " 00:00:00");
    }

    public static String getCurrentDateWithTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return (dtf.format(now));
    }

    public static String getCurrentDateWithTimeWith3Month() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return (dtf.format(now.plusMonths(3)));
    }


    public static Timestamp convertDateTotimeStamp(String inDate) {
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            return new Timestamp(((java.util.Date) df.parse(inDate)).getTime());
        } catch (Exception ie) {
            System.out.println("Problem creating directory " + ie);
        }
        return null;
    }


    public static Timestamp convertDateTotimeStampBookAppointment(String inDate) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return new Timestamp(((Date) df.parse(inDate+" 00:00:00")).getTime());
        } catch (Exception ie) {
            System.out.println("Problem creating directory " + ie);
        }
        return null;
    }

    public static Date convertDate(String inDate) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return new Timestamp(((Date) df.parse(inDate)).getTime());
        } catch (Exception ie) {
            System.out.println("Problem creating directory " + ie);
        }
        return null;
    }

    public static Timestamp getStartDate(String date) {
        date = date + " 00:00:00";
        DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        try {
            return new Timestamp(((Date) inputFormatter.parse(date)).getTime());
        } catch (Exception e) {

        }
        return null;
    }

    public static Timestamp getEndDate(String date) {
        date = date + " 23:59:59";
        DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return new Timestamp(((Date) inputFormatter.parse(date)).getTime());
        } catch (Exception e) {

        }
        return null;
    }


    public static Timestamp getNextYearDate() {
        Calendar cal = Calendar.getInstance();
        Date cdate = cal.getTime();
        cal.add(Calendar.YEAR, 1);
        Date nyear = cal.getTime();
        return new Timestamp(nyear.getTime());

    }


    public static String createdOtp() {

        Random rnd = new Random();
        Integer number = rnd.nextInt(999999);
        return String.format("%06d", number);

    }


    public static String encrypt(String str, String password) {
        try {
            String encryptedpassword = null;
            try {
                /* MessageDigest instance for MD5. */
                MessageDigest m = MessageDigest.getInstance("MD5");

                /* Add plain-text password bytes to digest using MD5 update() method. */
                m.update(password.getBytes());

                /* Convert the hash value into bytes */
                byte[] bytes = m.digest();

                /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
                StringBuilder s = new StringBuilder();
                for (Integer i = 0; i < bytes.length; i++) {
                    s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                }

                /* Complete hashed password in hexadecimal format */
                return encryptedpassword = s.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            /* Display the unencrypted and encrypted passwords. */
            System.out.println("Plain-text password: " + password);
            System.out.println("Encrypted password using MD5: " + encryptedpassword);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String str, String password) {
        try {
            byte[] ciphertext = DatatypeConverter.parseBase64Binary(str);
            if (ciphertext.length < 48) {
                return null;
            }
            byte[] salt = Arrays.copyOfRange(ciphertext, 0, 16);
            byte[] iv = Arrays.copyOfRange(ciphertext, 16, 32);
            byte[] ct = Arrays.copyOfRange(ciphertext, 32, ciphertext.length);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            byte[] plaintext = cipher.doFinal(ct);

            return new String(plaintext, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Date monthStartDate(String date, int day, int month) {
        Date startDate = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Calendar mCalendar = Calendar.getInstance();
        try {
            mCalendar.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "INVALID DATE FORMAT");
        }
        mCalendar.add(Calendar.DAY_OF_MONTH, -(day - 2));

        try {
            startDate = df.parse(df.format(mCalendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "INVALID DATE FORMAT");
        }
        return startDate;
    }


    public static Date monthEndDate(String date, int day, int month) {
        Date startDate = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Calendar mCalendar = Calendar.getInstance();
        try {
            mCalendar.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "INVALID DATE FORMAT");
        }
        mCalendar.add(Calendar.DAY_OF_MONTH, -(day - 1 - mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)));

        try {
            startDate = df.parse(df.format(mCalendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new SDDException(HttpStatus.UNAUTHORIZED.value(), "INVALID DATE FORMAT");
        }
        return startDate;
    }


}
