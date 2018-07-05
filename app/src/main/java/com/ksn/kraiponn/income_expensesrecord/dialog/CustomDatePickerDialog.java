package com.ksn.kraiponn.income_expensesrecord.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class CustomDatePickerDialog extends DialogFragment {

    public interface onFinishDialogListener{
        void onFinishDialog(int[] date);
    }

    private onFinishDialogListener mCallBack;


    public static CustomDatePickerDialog newInstance() {
        CustomDatePickerDialog dialog = new CustomDatePickerDialog();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }

    public void setOnFinishDialogListener(onFinishDialogListener listener) {
        mCallBack = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        int mm = cal.get(Calendar.MONTH);
        int yy = cal.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(
                getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view,
                                  int year, int month, int dayOfMonth) {
                int[] mDate = new int[3];
                mDate[0] = dayOfMonth;
                mDate[1] = (month+1);
                mDate[2] = year;
                mCallBack.onFinishDialog(mDate);
            }
        }, yy, mm, dd);

        return dialog;
    }


}
