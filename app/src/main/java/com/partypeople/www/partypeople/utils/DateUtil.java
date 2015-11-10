package com.partypeople.www.partypeople.utils;

import android.text.format.DateFormat;

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
        long first = changeStringToLong(firstDay);
        long second = changeStringToLong(secondDay);
        long diff = second - first;
        int day = (int)(diff / (24 * 60 * 60 * 1000));
        return day;
    }

    public String changeToViewFormat(String date) {
        long d = changeStringToLong(date);
        String result = DateFormat.format("MM월 dd일 / HH:mm", d).toString();
        return result;
    }
}