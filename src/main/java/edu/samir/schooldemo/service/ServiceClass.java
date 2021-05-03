package edu.samir.schooldemo.service;

import java.util.Base64;
import java.util.Date;

public class ServiceClass {

    public static class PathEncoder {

        public static String encode(String stringToEncode){
            return Base64.getEncoder().encodeToString(stringToEncode.getBytes());
        }

        public static String decode(String encodedPath){
            return new String(Base64.getDecoder().decode(encodedPath));
        }

    }

    public static class DateConverter{

        public static String dateToString(Date date){
            return String.valueOf(date.getTime());
        }

        public static Date stringToDate(String timeAsString){

            long time = Long.parseLong(timeAsString);
            Date date = new Date();
            date.setTime(time);
            return date;

        }
    }
}
