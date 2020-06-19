package net.zombie_sama.accountbook.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

public class YMPickerDialog extends DatePickerDialog {
    public YMPickerDialog(Context context, OnDateSetListener callback, int year, int monthOfYear, int dayOfMonth) {
        super(context, callback, year, monthOfYear, dayOfMonth);
        this.setTitle(year + "年" + (monthOfYear + 1) + "月");
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        this.setTitle(year + "年" + (month + 1) + "月");
    }

    @Override
    protected void onStop() {
    }
}
