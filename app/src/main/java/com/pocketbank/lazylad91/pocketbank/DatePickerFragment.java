package com.pocketbank.lazylad91.pocketbank;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Sagar on 7/27/2016.
 */
public  class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    static Button datepickerbtn;
    static TextView diplayView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
         datepickerbtn = (Button) getActivity().findViewById(R.id.transactiondate);
        diplayView = (TextView) getActivity().findViewById(R.id.dateTextView);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // set current date into datepicker
       // datepickerbtn.init(year, month, day, null);

        Log.d("Calendar", c.toString());

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        Log.d("Selected year", String.valueOf(year));
        Log.d("Selected month", String.valueOf(month));
        Log.d("Selected day", String.valueOf(day));
        AddTransactionActivity.tYear = year;
        AddTransactionActivity.tMonth = month;
        AddTransactionActivity.tDate = day;
//        diplayView.setText(String.valueOf(month)+"/"+String.valueOf(day)+"/"+String.valueOf(year));

        datepickerbtn.setText(String.valueOf(month)+"/"+String.valueOf(day)+"/"+String.valueOf(year));
    }
}
