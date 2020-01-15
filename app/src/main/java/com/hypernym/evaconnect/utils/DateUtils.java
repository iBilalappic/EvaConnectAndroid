package com.hypernym.evaconnect.utils;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateUtils {
    public static final String DATE_FORMAT_1 = "hh:mm a";
    public static final String DATE_FORMAT_2 = "d MMM";
    public static final String DATE_INPUT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SERVER_DATE_INPUT_FORMAT = "yyyy-MM-dd HH:mm:ss'UTC'";

    private DateUtils() {
        // This utility class is not publicly instantiable
    }

    public static String getFormattedDateTime(String datetime)
    {
        Date mParsedDate;
        String mOutputDateString="";
        String mOutputTimeString="";
        SimpleDateFormat mInputDateFormat =
                new SimpleDateFormat(DATE_INPUT_FORMAT, java.util.Locale.getDefault());
        SimpleDateFormat mOutputDateFormat =
                new SimpleDateFormat(DATE_FORMAT_1, java.util.Locale.getDefault());
        SimpleDateFormat mOutputDateFormat1 =
                new SimpleDateFormat(DATE_FORMAT_2, java.util.Locale.getDefault());
        try {
            datetime=convertServerDateToUserTimeZone(datetime);
            mParsedDate = mInputDateFormat.parse(datetime);
            mOutputDateString = mOutputDateFormat.format(mParsedDate);
            mOutputTimeString = mOutputDateFormat1.format(mParsedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mOutputDateString+" | "+mOutputTimeString;
    }

    public static String getTimeAgo(String datetime)
    {
        Date mParsedDate,currentParsedDate;
        SimpleDateFormat mInputDateFormat =
                new SimpleDateFormat(DATE_INPUT_FORMAT, java.util.Locale.getDefault());

        try {

            datetime= convertServerDateToUserTimeZone(datetime);
            mParsedDate = mInputDateFormat.parse(datetime);
            String currentDate=DateFormat.format(DATE_INPUT_FORMAT,Calendar.getInstance().getTime()).toString();
            currentParsedDate = mInputDateFormat.parse(currentDate);
            long difference=currentParsedDate.getTime()-mParsedDate.getTime();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = difference / daysInMilli;
            difference = difference % daysInMilli;

            long elapsedHours = difference / hoursInMilli;
            difference = difference % hoursInMilli;

            long elapsedMinutes = difference / minutesInMilli;
            difference = difference % minutesInMilli;

            long elapsedSeconds = difference / secondsInMilli;
            if(elapsedDays>0)
            {
                if(elapsedDays==1)
                {
                    return "("+elapsedDays+" day ago)";
                }
                else {
                    return "("+elapsedDays+" days ago)";
                }
            }
            else if(elapsedHours >0)
            {
                if(elapsedHours==1)
                {
                    return "("+elapsedHours+" hour ago)";
                }
                else {
                    return "("+elapsedHours+" hours ago)";
                }

            }
            else if(elapsedMinutes>0)
            {
                if(elapsedMinutes==1)
                {
                    return "("+elapsedMinutes+" min ago)";
                }
                else {
                    return "("+elapsedMinutes+" mins ago)";
                }
            }
            else if(elapsedSeconds>0)
            {
                if(elapsedSeconds==1)
                {
                    return "("+elapsedSeconds+" sec ago)";
                }
                else {
                    return "("+elapsedSeconds+" secs ago)";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "(Just Now)";
    }
    public static String convertServerDateToUserTimeZone(String serverDate) {
        String ourdate;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_INPUT_FORMAT);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(serverDate);
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_INPUT_FORMAT); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourdate = dateFormatter.format(value);

            Log.d("OurDate", ourdate);
        } catch (Exception e) {
            ourdate = "0000-00-00 00:00:00";
        }
        return ourdate;
    }
}