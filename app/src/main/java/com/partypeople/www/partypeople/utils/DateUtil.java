package com.partypeople.www.partypeople.utils;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tacademy on 2015-11-10.
 */
public class DateUtil {
    private static DateUtil instance;
    public static DateUtil getInstance() {
        if (instance == null) {
            instance = new DateUtil();
        }
        return instance;
    }

    public String getCurrentDate() {
        long time= System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String date = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss", cal).toString();
        return date;
    }

    public String changeLongToString(long input) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(input);
        String date = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss", cal).toString();
        return date;
    }

    public long changeStringToLong(String input) {
        if(input == null)
            return 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = (Date)formatter.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long mills = date.getTime();
        return mills;
    }

    public int getDiffDay(String firstDay, String secondDay) {
        if(firstDay == null || secondDay == null)
            return 0;
        long first = changeStringToLong(firstDay);
        long second = changeStringToLong(secondDay);
        long diff = second - first;
        int day = (int)(diff / (24 * 60 * 60 * 1000));
        return day;
    }

    public String changeToViewFormat(String date) {
        if(date == null)
            return null;
        long d = changeStringToLong(date);
        String result = DateFormat.format("MM월 dd일 / HH:mm", d).toString();
        return result;
    }

    public int getDayOfMonth(String year, String month) {
        Date date = new Date();
//        String yearAndmonth = year + month;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        try {
            date = formatter.parse(year + month);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
