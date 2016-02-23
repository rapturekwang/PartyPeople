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

    public String getDefaultSettingData(int day, long today) {
        long time = today + (day * 24 * 60 * 60 * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String date = DateFormat.format("yyyy:MM:dd:오후:08:00", cal).toString();

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
            date = formatter.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
        long mills = date.getTime();
        return mills;
    }

    public boolean compare(String previousDay, String nextDay) {
        long previous = changeStringToLong(previousDay);
        long next = changeStringToLong(nextDay);
        if(previous<next)
            return true;
        else
            return false;
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

    public int getDiffDay(String day) {
        int diff = getDiffDay(day, getCurrentDate());
        return diff;
    }

    public int getDiffHour(String firstHour, String secondHour) {
        if(firstHour == null || secondHour == null)
            return 0;
        long first = changeStringToLong(firstHour);
        long second = changeStringToLong(secondHour);
        long diff = second - first;
        int hour = (int)(diff / (60 * 60 * 1000));
        return  hour;
    }

    public int getDiffHour(String hour) {
        int diff = getDiffHour(hour, getCurrentDate());
        return  diff;
    }

    public String changeToViewFormat(String start) {
        if(start==null)
            return null;

        long d = changeStringToLong(start);

        return DateFormat.format("MM월 dd일 / HH:mm", d).toString();
    }

    public String getSharingFormat(String partyTime) {
        long time = changeStringToLong(partyTime);
        int hour = Integer.parseInt(DateFormat.format("HH", time).toString());

        String result;
        result = DateFormat.format("yyyy년 MM월 dd일 ", time).toString();
        if(hour == 12) {
            result += "오후 " + hour;
        } else if(hour == 0) {
            result += "오전 " + 12;
        } else if(hour > 12) {
            result += "오후 " + (hour-12);
        } else {
            result += "오전 " + hour;
        }
        result += DateFormat.format(":mm", time).toString();

        return result;
    }

    public int getDayOfMonth(String year, String month) {
        Date date = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        try {
            date = formatter.parse(year + month);
        } catch (ParseException e) {
            e.printStackTrace();
            return 30;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public String changeToPostFormat(String date) {
        Long temp = changeStringToLong(date);
        return changeLongToString(temp);
    }

    public String getDueDate() {
        long time= System.currentTimeMillis();
        time += 2 * 24 * 60 * 60 * 1000;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        String dueDate = DateFormat.format("yyyyMMdd", cal).toString();

        return String.valueOf(dueDate);
    }

    public String getPaymentTime(String time) {
        String array[] = time.split("partypeopl");
        array[1] = array[1].substring(0, 14);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = formatter.parse(array[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long mills = date.getTime();
        return changeLongToString(mills);
    }
}
