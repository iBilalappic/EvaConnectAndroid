package com.hypernym.evaconnect.utils;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateUtils {
    public static final String DATE_FORMAT_1 = "hh:mm a";
    public static final String DATE_FORMAT_2 = "d MMM";
    public static final String DATE_FORMAT_3 = "d MMM yyyy";
    public static final String DATE_FORMAT_4 = "MMM";
    public static final String DATE_FORMAT_5 = "dd";
    public static final String TIME_FORMAT_1 = "HH:mm:ss";
    public static final String DATE_INPUT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_INPUT_FORMAT_WITHOUTTIME = "yyyy-MM-dd";
    public static final String SERVER_DATE_INPUT_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String EVENT_DATE_INPUT_FORMAT = "E, dd MMM yyyy";


    private DateUtils() {
        // This utility class is not publicly instantiable
    }

    public static String getFormattedDateTime(String datetime) {
        Date mParsedDate;
        String mOutputDateString = "";
        String mOutputTimeString = "";
        SimpleDateFormat mInputDateFormat =
                new SimpleDateFormat(DATE_INPUT_FORMAT, java.util.Locale.getDefault());
        SimpleDateFormat mOutputDateFormat =
                new SimpleDateFormat(DATE_FORMAT_1, java.util.Locale.getDefault());
        SimpleDateFormat mOutputDateFormat1 =
                new SimpleDateFormat(DATE_FORMAT_2, java.util.Locale.getDefault());
        try {
            datetime = convertServerDateToUserTimeZone(datetime);
            mParsedDate = mInputDateFormat.parse(datetime);
            mOutputDateString = mOutputDateFormat.format(mParsedDate);
            mOutputTimeString = mOutputDateFormat1.format(mParsedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mOutputDateString + " | " + mOutputTimeString;
    }

    public static String getFormattedTime(String datetime) {
        Date mParsedDate;
        String mOutputDateString = "";
        String mOutputTimeString = "";
        SimpleDateFormat mInputDateFormat =
                new SimpleDateFormat(DATE_INPUT_FORMAT, java.util.Locale.getDefault());

        SimpleDateFormat mOutputDateFormat1 =
                new SimpleDateFormat(DATE_FORMAT_1, java.util.Locale.getDefault());
        try {
            datetime = convertServerDateToUserTimeZone(datetime);
            mParsedDate = mInputDateFormat.parse(datetime);
            mOutputTimeString = mOutputDateFormat1.format(mParsedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mOutputTimeString;
    }

    public static String getFormattedEventTime(String datetime) {
        Date mParsedDate;
        String mOutputDateString = "";
        String mOutputTimeString = "";
        SimpleDateFormat mInputDateFormat =
                new SimpleDateFormat(TIME_FORMAT_1, java.util.Locale.getDefault());

        SimpleDateFormat mOutputDateFormat1 =
                new SimpleDateFormat(DATE_FORMAT_1, java.util.Locale.getDefault());
        try {

            mParsedDate = mInputDateFormat.parse(datetime);
            mOutputTimeString = mOutputDateFormat1.format(mParsedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mOutputTimeString;
    }

    public static String extractMonth(String date) {
        Date mParsedDate;
        String mOutputDateString = "";
        String mOutputTimeString = "";
        SimpleDateFormat mInputDateFormat =
                new SimpleDateFormat(DATE_INPUT_FORMAT_WITHOUTTIME, java.util.Locale.getDefault());

        SimpleDateFormat mOutputDateFormat1 =
                new SimpleDateFormat(DATE_FORMAT_4, java.util.Locale.getDefault());
        try {

            mParsedDate = mInputDateFormat.parse(date);
            mOutputTimeString = mOutputDateFormat1.format(mParsedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mOutputTimeString;
    }

    public static String extractDay(String date) {
        Date mParsedDate;
        String mOutputDateString = "";
        String mOutputTimeString = "";
        SimpleDateFormat mInputDateFormat =
                new SimpleDateFormat(DATE_INPUT_FORMAT_WITHOUTTIME, java.util.Locale.getDefault());

        SimpleDateFormat mOutputDateFormat1 =
                new SimpleDateFormat(DATE_FORMAT_5, java.util.Locale.getDefault());
        try {

            mParsedDate = mInputDateFormat.parse(date);
            mOutputTimeString = mOutputDateFormat1.format(mParsedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mOutputTimeString;
    }

    public static String getFormattedDateDMY(String datetime) {
        Date mParsedDate;
        String mOutputDateString = "";

        SimpleDateFormat mInputDateFormat =
                new SimpleDateFormat(DATE_INPUT_FORMAT_WITHOUTTIME, java.util.Locale.getDefault());
        SimpleDateFormat mOutputDateFormat =
                new SimpleDateFormat(DATE_FORMAT_3, java.util.Locale.getDefault());

        try {

            // datetime = convertServerDateToUserTimeZone(datetime);
            mParsedDate = mInputDateFormat.parse(datetime);
            mOutputDateString = mOutputDateFormat.format(mParsedDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mOutputDateString;
    }

    public static String getFormattedEventDate(String datetime) {
        String formatedDate = null;
        try {
            datetime = convertEventDateToUserTimeZone(datetime);

            SimpleDateFormat format = new SimpleDateFormat(DATE_INPUT_FORMAT);
            Date newDate = format.parse(datetime);

            format = new SimpleDateFormat(DATE_INPUT_FORMAT_WITHOUTTIME);
            formatedDate = format.format(newDate);
            return formatedDate;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return formatedDate;
    }
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_INPUT_FORMAT_WITHOUTTIME);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
    public static boolean isValidTime(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(time.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }


    public static String getFormattedEventDateDMY(String datetime) {
        String formatedDate = null;
        try {
            //datetime = convertEventDateToUserTimeZone(datetime);

            SimpleDateFormat format = new SimpleDateFormat(DATE_INPUT_FORMAT);
            Date newDate = format.parse(datetime);

            format = new SimpleDateFormat(DATE_INPUT_FORMAT_WITHOUTTIME);
            formatedDate = format.format(newDate);
            return formatedDate;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return formatedDate;
    }

    public static String getFormattedDate(String datetime) {
        Date mParsedDate;
        String mOutputDateString = "";
        String mOutputTimeString = "";
        SimpleDateFormat mInputDateFormat =
                new SimpleDateFormat(DATE_INPUT_FORMAT, java.util.Locale.getDefault());
        SimpleDateFormat mOutputDateFormat =
                new SimpleDateFormat(DATE_FORMAT_1, java.util.Locale.getDefault());
        SimpleDateFormat mOutputDateFormat1 =
                new SimpleDateFormat(DATE_FORMAT_2, java.util.Locale.getDefault());
        try {
            datetime = convertServerDateToUserTimeZone(datetime);
            mParsedDate = mInputDateFormat.parse(datetime);
            mOutputDateString = mOutputDateFormat.format(mParsedDate);
            mOutputTimeString = mOutputDateFormat1.format(mParsedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mOutputDateString;
    }

    public static String Getdatetime() {
        Date currentParsedDate;
        SimpleDateFormat mInputDateFormat =
                new SimpleDateFormat(DATE_INPUT_FORMAT, java.util.Locale.getDefault());
        try {
            String currentDate = DateFormat.format(DATE_INPUT_FORMAT, Calendar.getInstance().getTime()).toString();
            currentParsedDate = mInputDateFormat.parse(currentDate);
            return currentParsedDate.toString();
        } catch (Exception ex) {

        }

        return "-";
    }

    public static String GetCurrentdatetime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        String completetime = df.format(Calendar.getInstance().getTime());
        return completetime;
    }
    public static String GetCurrentUTCTime() {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        String completetime = df.format(Calendar.getInstance().getTime());
        return completetime;
    }

    public static String Gettime_UTC() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        String completetime = df.format(Calendar.getInstance().getTime());
        return completetime;
    }


    public static String GetCurrentdate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        String completetime = df.format(Calendar.getInstance().getTime());
        return completetime;
    }

    public static String convertnumtocharmonths(int m) {
        String charname = null;
        if (m == 1) {
            charname = "Jan";
        }
        if (m == 2) {
            charname = "Feb";
        }
        if (m == 3) {
            charname = "Mar";
        }
        if (m == 4) {
            charname = "April";
        }
        if (m == 5) {
            charname = "May";
        }
        if (m == 6) {
            charname = "June";
        }
        if (m == 7) {
            charname = "July";
        }
        if (m == 8) {
            charname = "Aug";
        }
        if (m == 9) {
            charname = "Sep";
        }
        if (m == 10) {
            charname = "Oct";
        }
        if (m == 11) {
            charname = "Nov";
        }
        if (m == 12) {
            charname = "Dec";
        }
        return charname;
    }


    public static String getTimeAgo(String datetime) {
        Date mParsedDate, currentParsedDate;
        SimpleDateFormat mInputDateFormat =
                new SimpleDateFormat(DATE_INPUT_FORMAT, java.util.Locale.getDefault());

        try {

            datetime = convertServerDateToUserTimeZone(datetime);
            mParsedDate = mInputDateFormat.parse(datetime);
            String currentDate = DateFormat.format(DATE_INPUT_FORMAT, Calendar.getInstance().getTime()).toString();
            currentParsedDate = mInputDateFormat.parse(currentDate);
            long difference = currentParsedDate.getTime() - mParsedDate.getTime();
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
            if (elapsedDays > 0) {
                if (elapsedDays == 1) {
                    return "(" + elapsedDays + " day ago)";
                } else {
                    return "(" + elapsedDays + " days ago)";
                }
            } else if (elapsedHours > 0) {
                if (elapsedHours == 1) {
                    return "(" + elapsedHours + " hour ago)";
                } else {
                    return "(" + elapsedHours + " hours ago)";
                }

            } else if (elapsedMinutes > 0) {
                if (elapsedMinutes == 1) {
                    return "(" + elapsedMinutes + " min ago)";
                } else {
                    return "(" + elapsedMinutes + " mins ago)";
                }
            } else if (elapsedSeconds > 0) {
                if (elapsedSeconds == 1) {
                    return "(" + elapsedSeconds + " sec ago)";
                } else {
                    return "(" + elapsedSeconds + " secs ago)";
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

    public static String convertEventDateToUserTimeZone(String serverDate) {
        String ourdate;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(EVENT_DATE_INPUT_FORMAT);
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

    public static String getDate(String date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date serverDate = simpleDateFormat.parse(date);
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTime(serverDate);
//                calendar.add(Calendar.MILLISECOND,
//                        TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return dateFormat.format(serverDate);
            } catch (Exception e) {
                Log.e(">> Date Exception", e.getMessage());
            }
        }
        return "-";
    }

    public static String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    public static boolean compareDates(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {

            Date pickupDateTime = sdf.parse(date);

            Calendar now = Calendar.getInstance();
            Calendar givenDate = Calendar.getInstance();
            givenDate.setTime(pickupDateTime);
            if (!now.before(givenDate) && !now.getTime().equals(givenDate)) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static String getTime_utc(String date) {
//        if (date != null) {
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
//            try {
//                Date serverDate = simpleDateFormat.parse(date);
//                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//                calendar.setTime(serverDate);
//                calendar.add(Calendar.MILLISECOND,
//                        TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings());
//                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
//                return dateFormat.format(calendar.getTime());
//            } catch (Exception e) {
//                Log.e(">> Date Exception", e.getMessage());
//            }
//        }
//        return "-";
        Date mParsedDate;
        String mOutputDateString = "";
        String mOutputTimeString = "";
        SimpleDateFormat mInputDateFormat =
                new SimpleDateFormat("hh:mm aa", java.util.Locale.getDefault());

        SimpleDateFormat mOutputDateFormat1 =
                new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
        try {

            mParsedDate = mInputDateFormat.parse(date);
            mOutputTimeString = mOutputDateFormat1.format(mParsedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mOutputTimeString;

    }

    public static String get12formant(String date) {

        Date mParsedDate;
        String mOutputDateString = "";
        String mOutputTimeString = "";
        SimpleDateFormat mInputDateFormat =
                new SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault());

        SimpleDateFormat mOutputDateFormat1 =
                new SimpleDateFormat("hh:mm aa", java.util.Locale.getDefault());
        try {

            mParsedDate = mInputDateFormat.parse(date);
            mOutputTimeString = mOutputDateFormat1.format(mParsedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mOutputTimeString;

    }

    public static String formatToYesterdayOrToday(String date) {
        try {
            Date dateTime = new SimpleDateFormat(DATE_INPUT_FORMAT).parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateTime);
            Calendar today = Calendar.getInstance();
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -1);
            SimpleDateFormat timeFormatter = new SimpleDateFormat(DATE_INPUT_FORMAT);

            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date newdate = timeFormatter.parse(date);
                timeFormatter.setTimeZone(TimeZone.getDefault());
                SimpleDateFormat newtimeFormatter = new SimpleDateFormat("hh:mm aa");
                return "Today at " + newtimeFormatter.format(newdate);
            }  else {
                timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date newdate = timeFormatter.parse(date);
                timeFormatter.setTimeZone(TimeZone.getDefault());
                SimpleDateFormat  mInputDateFormat= new SimpleDateFormat("EEEE, hh:mm aa");
                return mInputDateFormat.format(newdate);

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Just Now";
    }
    public static String getDateTimeFromTimestamp(String time) {
        long longTime = Long.parseLong(time);

        Date date = new Date(longTime); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, hh:mm aa"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setTimeZone(TimeZone.getDefault());
       return sdf.format(date);
    }

    public static Date formattedDateWithoutTime(String date)
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_INPUT_FORMAT_WITHOUTTIME, Locale.US);
            Date newdate = dateFormat.parse(date);
            return newdate;
        }
        catch(ParseException pe ) {
            // handle the failure
        }
        return new Date();
    }

    public static Date formattedDateTime(String time)
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT_1, Locale.US);
            Date newdate = dateFormat.parse(time);
            return newdate;
        }
        catch(ParseException pe ) {
            // handle the failure
        }
        return new Date();
    }
}
