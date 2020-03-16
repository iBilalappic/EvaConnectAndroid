package com.hypernym.evaconnect.dateTimePicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TabHost;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class DateTimePicker extends DialogFragment {
    public static final String TAG_FRAG_DATE_TIME = "fragDateTime";
    private static final String KEY_DIALOG_TITLE = "dialogTitle";
    private static final String KEY_INIT_DATE = "initDate";
    private static final String KEY_MIN_DATE = "minDate";
    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";
    private Context mContext;
    private ButtonClickListener mButtonClickListener;
    private OnDateTimeSetListener mOnDateTimeSetListener;
    private Bundle mArgument;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private static Date minDate;
    // DialogFragment constructor must be empty
    public DateTimePicker() {
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mButtonClickListener = new ButtonClickListener();
    }
    /**
     *
     * @param dialogTitle Title of the DateTimePicker DialogFragment
     * @param initDate Initial Date and Time set to the Date and Time Picker
     * @return Instance of the DateTimePicker DialogFragment
     */
    public static DateTimePicker newInstance(CharSequence dialogTitle, Date initDate,Date minimumDate) {
        // Create a new instance of DateTimePicker
        DateTimePicker mDateTimePicker = new DateTimePicker();
        // Setup the constructor parameters as arguments
        Bundle mBundle = new Bundle();
        mBundle.putCharSequence(KEY_DIALOG_TITLE, dialogTitle);
        mBundle.putSerializable(KEY_INIT_DATE, initDate);
        mBundle.putSerializable(KEY_MIN_DATE, minDate);
        mDateTimePicker.setArguments(mBundle);
        minDate=minimumDate;
        // Return instance with arguments
        return mDateTimePicker;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Retrieve Argument passed to the constructor
        mArgument = getArguments();
        // Use an AlertDialog Builder to initially create the Dialog
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        // Setup the Dialog
        mBuilder.setTitle(mArgument.getCharSequence(KEY_DIALOG_TITLE));
        mBuilder.setNegativeButton(android.R.string.no,mButtonClickListener);

        mBuilder.setPositiveButton(android.R.string.yes,mButtonClickListener);
        // Create the Alert Dialog
        AlertDialog mDialog = mBuilder.create();
        // Set the View to the Dialog
        mDialog.setView(
                createDateTimeView(mDialog.getLayoutInflater())
        );
        //2. now setup to change color of the button
        mDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            }
        });

        // Return the Dialog created
        return mDialog;
    }
    /**
     * Inflates the XML Layout and setups the tabs
     * @param layoutInflater Layout inflater from the Dialog
     * @return Returns a view that will be set to the Dialog
     */
    private View createDateTimeView(LayoutInflater layoutInflater) {
        // Inflate the XML Layout using the inflater from the created Dialog
        View mView = layoutInflater.inflate(R.layout.date_time_picker,null);
        // Extract the TabHost
        TabHost mTabHost = (TabHost) mView.findViewById(R.id.tab_host);
        mTabHost.setup();
        // Create Date Tab and add to TabHost
        TabHost.TabSpec mDateTab = mTabHost.newTabSpec(TAG_DATE);
        mDateTab.setIndicator(getString(R.string.tab_date));
        mDateTab.setContent(R.id.date_content);
        mTabHost.addTab(mDateTab);
        // Create Time Tab and add to TabHost
        TabHost.TabSpec mTimeTab = mTabHost.newTabSpec(TAG_TIME);
        mTimeTab.setIndicator(getString(R.string.tab_time));
        mTimeTab.setContent(R.id.time_content);
        mTabHost.addTab(mTimeTab);
        // Retrieve Date from Arguments sent to the Dialog
        DateTime mDateTime = new DateTime((Date) mArgument.getSerializable(KEY_INIT_DATE));
        // Initialize Date and Time Pickers
        mDatePicker = (DatePicker) mView.findViewById(R.id.date_picker);
        mTimePicker = (TimePicker) mView.findViewById(R.id.time_picker);
        mDatePicker.init(mDateTime.getYear(), mDateTime.getMonthOfYear(),
                mDateTime.getDayOfMonth(), null);
        mDatePicker.setMinDate(minDate.getTime());

        mTimePicker.setCurrentHour(mDateTime.getHourOfDay());
        mTimePicker.setCurrentMinute(mDateTime.getMinuteOfHour());
        // Return created view
        return mView;
    }
    /**
     * Sets the OnDateTimeSetListener interface
     * @param onDateTimeSetListener Interface that is used to send the Date and Time
     *               to the calling object
     */
    public void setOnDateTimeSetListener(OnDateTimeSetListener onDateTimeSetListener) {
        mOnDateTimeSetListener = onDateTimeSetListener;
    }
    private class ButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int result) {
            // Determine if the user selected Ok
            try {
                if(DialogInterface.BUTTON_POSITIVE == result) {
                    DateTime mDateTime = new DateTime(
                            mDatePicker.getYear(),
                            mDatePicker.getMonth(),
                            mDatePicker.getDayOfMonth(),
                            mTimePicker.getCurrentHour(),
                            mTimePicker.getCurrentMinute()
                    );
                    if(DateUtils.compareDates(mDateTime.getDateString()))
                    {
                        mOnDateTimeSetListener.DateTimeSet(mDateTime.getDate());
                    }
                    else
                    {
                        Toast.makeText(getContext(),getString(R.string.msg_invalid_datetime),Toast.LENGTH_LONG).show();
                    }

                }
            }
            catch (ParseException ex)
            {
                Log.e("Parse exception",ex.getMessage().toString());
            }

        }
    }
    /**
     * Interface for sending the Date and Time to the calling object
     */
    public interface OnDateTimeSetListener {
        public void DateTimeSet(Date date) throws ParseException;
    }
}