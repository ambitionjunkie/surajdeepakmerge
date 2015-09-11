package com.attendanceapp.utils;

import com.attendanceapp.models.User;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public final class StringUtils {
    final static String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static String getAllIdsFromStudentList(List<? extends User> studentList, char separator) {

        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<? extends User> iterator = studentList.iterator();

        while (iterator.hasNext()) {
            User student = iterator.next();
            bodyBuilder.append(student.getUserId());

            if (iterator.hasNext()) {
                bodyBuilder.append(separator);
            }
        }

        return bodyBuilder.toString();
    }

    public static String getAllEmailsFromStudentList(List<? extends User> studentList, char separator) {

        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<? extends User> iterator = studentList.iterator();

        while (iterator.hasNext()) {
            User student = iterator.next();
            bodyBuilder.append(student.getEmail());

            if (iterator.hasNext()) {
                bodyBuilder.append(separator);
            }
        }

        return bodyBuilder.toString();
    }

    public static String changeFirstLetterToUppercase(String textToChange) {
        if (textToChange == null) {
            return "";
        }
        if (textToChange.length() == 1) {
            return textToChange.toUpperCase();
        }
        textToChange = (String.valueOf(textToChange.charAt(0)).toUpperCase()) + textToChange.substring(1).toLowerCase();
        return textToChange;
    }

    public static String getTimeStringFromCalender(Calendar cal) {
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        //return hourOfDay+":"+minute;
        return getTimeStringFromHourMinute(hourOfDay,minute);
    }


    public static String getTimeStringFromHourMinute(int hourOfDay,int minute) {
        String state = "am";
        if (hourOfDay > 12) {
            hourOfDay -= 12;
            state = "pm";
        } else if (hourOfDay == 0) {
            hourOfDay = 12;
        } else if (hourOfDay == 12) {
            state = "pm";
        }
        String minuteString = String.valueOf(minute).length() < 2 ? "0" + minute : "" + minute;
        return ("" + hourOfDay + ":" + minuteString + " " + state);
    }


    public static String getDateStringFromCalender(Calendar cal) {
        return "" + MONTHS[cal.get(Calendar.MONTH) + 1] + " "
                + cal.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR);
    }
}
