package com.hypernym.evaconnect.dateTimePicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Date;

public class SimpleDateTimePicker {

    private CharSequence mDialogTitle;
    private Date mInitDate,minimumDate;
    private DateTimePicker.OnDateTimeSetListener mOnDateTimeSetListener;
    private FragmentManager mFragmentManager;

    /**
     * Private constructor that can only be access using the make method
     * @param dialogTitle Title of the Date Time Picker Dialog
     * @param initDate Date object to use to set the initial Date and Time
     * @param onDateTimeSetListener OnDateTimeSetListener interface
     * @param fragmentManager Fragment Manager from the activity
     */
    private SimpleDateTimePicker(CharSequence dialogTitle, Date initDate,Date minDate,
                                 DateTimePicker.OnDateTimeSetListener onDateTimeSetListener,
                                 FragmentManager fragmentManager) {

        // Find if there are any DialogFragments from the FragmentManager
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        Fragment mDateTimeDialogFrag = fragmentManager.findFragmentByTag(
                DateTimePicker.TAG_FRAG_DATE_TIME
        );

        // Remove it if found
        if(mDateTimeDialogFrag != null) {
            mFragmentTransaction.remove(mDateTimeDialogFrag);
        }
        mFragmentTransaction.addToBackStack(null);

        mDialogTitle = dialogTitle;
        mInitDate = initDate;
        minimumDate=minDate;
        mOnDateTimeSetListener = onDateTimeSetListener;
        mFragmentManager = fragmentManager;

    }

    /**
     * Creates a new instance of the SimpleDateTimePicker
     * @param dialogTitle Title of the Date Time Picker Dialog
     * @param initDate Date object to use to set the initial Date and Time
     * @param onDateTimeSetListener OnDateTimeSetListener interface
     * @param fragmentManager Fragment Manager from the activity
     * @return Returns a SimpleDateTimePicker object
     */
    public static SimpleDateTimePicker make(CharSequence dialogTitle, Date initDate,Date minDate,
                                            DateTimePicker.OnDateTimeSetListener onDateTimeSetListener,
                                            FragmentManager fragmentManager) {

        return new SimpleDateTimePicker(dialogTitle, initDate,minDate,
                onDateTimeSetListener, fragmentManager);

    }

    /**
     * Shows the DateTimePicker Dialog
     */
    public void show() {

        // Create new DateTimePicker
        DateTimePicker mDateTimePicker = DateTimePicker.newInstance(mDialogTitle,mInitDate,minimumDate);
        mDateTimePicker.setOnDateTimeSetListener(mOnDateTimeSetListener);
        mDateTimePicker.show(mFragmentManager, DateTimePicker.TAG_FRAG_DATE_TIME);

    }
}
