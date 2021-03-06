package com.krp.android.recyclerwithrecycler.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.krp.android.recyclerwithrecycler.R;
import com.marcohc.robotocalendar.RobotoCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Created by purushottam.kumar on 12/15/2015.
 */
public final class CalendarDialog extends DialogFragment implements RobotoCalendarView.RobotoCalendarListener {

    public static final String TAG = CalendarDialog.class.getSimpleName();
    public static final String FORMAT_SIMPLE_DATE = "dd/MM/yyyy";

    private RobotoCalendarView robotoCalendarView;
    private int currentMonthIndex;
    private Calendar currentCalendar;
    private OnSelectDateListener dateListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Gets the calendar from the view
        View rootView = inflater.inflate(R.layout.calendar, null);
        robotoCalendarView = (RobotoCalendarView) rootView.findViewById(R.id.robotoCalendarPicker);
        robotoCalendarView.setRobotoCalendarListener(this);

        // Initialize the RobotoCalendarPicker with the current index and date
        currentMonthIndex = 0;
        currentCalendar = Calendar.getInstance(Locale.getDefault());

        try {
            currentCalendar.setTime(new SimpleDateFormat(FORMAT_SIMPLE_DATE)
                    .parse(getArguments().getString("date")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Mark current day
        robotoCalendarView.initializeCalendar(currentCalendar);
        robotoCalendarView.markDayAsSelectedDay(currentCalendar.getTime());

        builder.setView(rootView);
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof OnSelectDateListener) {
            dateListener = (OnSelectDateListener) activity;
        } else {
            Log.e(TAG, activity.getClass().getSimpleName() +
                    " class must implement " + OnSelectDateListener.class.getSimpleName());
        }
    }

    private void updateCalendar() {
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        robotoCalendarView.initializeCalendar(currentCalendar);
    }

    public interface OnSelectDateListener {
        void onDateSelected(Date date);
    }

    @Override
    public void onDateSelected(Date date) {
        // call back to UI listeners
        if(dateListener != null) {
            dateListener.onDateSelected(date);
        }
    }

    @Override
    public void onRightButtonClick() {
        currentMonthIndex++;
        updateCalendar();
    }

    @Override
    public void onLeftButtonClick() {
        currentMonthIndex--;
        updateCalendar();
    }
}
