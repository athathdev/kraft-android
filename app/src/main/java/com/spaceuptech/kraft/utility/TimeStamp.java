package com.spaceuptech.kraft.utility;


import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class TimeStamp {
    private static final long TIME_PERIOD_ONE_MINUTE = 60000L;
    private static final long TIME_PERIOD_ONE_HOUR = 3600000L;
    private static final long TIME_PERIOD_ONE_DAY = 86400000L;
    private static final long TIME_PERIOD_ONE_WEEK = 604800000L;
    private static final long TIME_PERIOD_ONE_MONTH = 2592000000L;
    private static final long TIME_PERIOD_ONE_YEAR = 31104000000L;
    public static String getTime(long time){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date today = cal.getTime();
        Date date = new Date(time);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy",new Locale("EN","IN"));
        if (dateFormat.format(date).equals(dateFormat.format(today))) {
            dateFormat = new SimpleDateFormat("hh:mm:aa",new Locale("EN","IN"));
            return dateFormat.format(date);
        }
        else if (dateFormat.format(date).equals(dateFormat.format(yesterday))){
            return "Yesterday";
        }
        else{
            dateFormat = new SimpleDateFormat("dd/MM/yy",new Locale("EN","IN"));
            return dateFormat.format(date);
        }
    }

    public static String getDate(long time){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date today = cal.getTime();
        Date date = new Date(time);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy",new Locale("EN","IN"));
        if (dateFormat.format(date).equals(dateFormat.format(today))) {
            return "Today";
        }
        else if (dateFormat.format(date).equals(dateFormat.format(yesterday))){
            return "Yesterday";
        }
        else{
            dateFormat = new SimpleDateFormat("dd MMMM yyyy",new Locale("EN","IN"));
            return dateFormat.format(date);
        }
    }

    public static String getExactTime(long time){
        Date date = new Date(time);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:aa",new Locale("EN","IN"));
        return dateFormat.format(date);
    }

    public static String getTimeElapsed(long time){
        Date date = new Date();
        long timeNow = date.getTime();
        if ((timeNow - time) < TIME_PERIOD_ONE_MINUTE ) return "Just now";
        else if ((timeNow - time) < TIME_PERIOD_ONE_HOUR){
            if ((timeNow - time) < 2*TIME_PERIOD_ONE_MINUTE )
            return "1 min ago";
            else return String.valueOf((timeNow - time)/TIME_PERIOD_ONE_MINUTE) + " mins ago";
        }
        else if ((timeNow - time) < TIME_PERIOD_ONE_DAY){
            if ((timeNow - time) < 2*TIME_PERIOD_ONE_HOUR )
                return "1 hour ago";
            else return String.valueOf((timeNow - time)/TIME_PERIOD_ONE_HOUR) + " hours ago";
        }
        else if ((timeNow - time) < TIME_PERIOD_ONE_WEEK){
            if ((timeNow - time) < 2*TIME_PERIOD_ONE_DAY )
                return "1 day ago";
            else return String.valueOf((timeNow - time)/TIME_PERIOD_ONE_DAY) + " days ago";
        }
        else if ((timeNow - time) < TIME_PERIOD_ONE_MONTH){
            if ((timeNow - time) < 2*TIME_PERIOD_ONE_WEEK )
                return "1 week ago";
            else return String.valueOf((timeNow - time)/TIME_PERIOD_ONE_WEEK) + " weeks ago";
        }
        else if ((timeNow - time) < TIME_PERIOD_ONE_YEAR){
            if ((timeNow - time) < 2*TIME_PERIOD_ONE_MONTH )
                return "1 month ago";
            else return String.valueOf((timeNow - time)/TIME_PERIOD_ONE_MONTH) + " months ago";
        }
        else {
            if ((timeNow - time) < 2*TIME_PERIOD_ONE_YEAR )
                return "1 year ago";
            else return String.valueOf((timeNow - time)/TIME_PERIOD_ONE_MONTH) + " years ago";
        }

    }
}
