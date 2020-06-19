package net.zombie_sama.accountbook.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import net.zombie_sama.accountbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class MonthPicker {
    public static final int YEAR_MIN = 2000;
    public static final int YEAR_MAX = 2999;
    public static final int MONTH_MIN = 1;
    public static final int MONTH_MAX = 12;

    public static void showPicker(Context context, int initYear, int initMonth, final DateSetListener listener) {
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_month_picker, null);
        final NumberPicker npYear = ButterKnife.findById(root, R.id.np_year);
        final NumberPicker npMonth = ButterKnife.findById(root, R.id.np_month);
        npYear.setMinValue(YEAR_MIN);
        npYear.setMaxValue(YEAR_MAX);
        npMonth.setMinValue(MONTH_MIN);
        npMonth.setMaxValue(MONTH_MAX);

        if (initYear < YEAR_MIN) initYear = YEAR_MIN;
        else if (initYear > YEAR_MAX) initYear = YEAR_MAX;
        if (initMonth < MONTH_MIN) initMonth = MONTH_MIN;
        else if (initMonth > MONTH_MAX) initMonth = MONTH_MAX;

        npYear.setValue(initYear);
        npMonth.setValue(initMonth);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("选择年月");
        builder.setView(root);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDateSet(npYear.getValue(), npMonth.getValue());
                dialog.cancel();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public interface DateSetListener {
        void onDateSet(int year, int month);
    }
}
