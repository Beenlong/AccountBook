package net.zombie_sama.accountbook.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.zombie_sama.accountbook.R;
import net.zombie_sama.accountbook.adapter.EntryAddCategoryAdatper;
import net.zombie_sama.accountbook.base.BaseActivity;
import net.zombie_sama.accountbook.base.BaseApplication;
import net.zombie_sama.accountbook.database.table.Category;
import net.zombie_sama.accountbook.database.table.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntryAddActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_category_manage)
    TextView tvCategoryManage;
    @BindView(R.id.spinner_category)
    Spinner spinnerCategory;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_income)
    TextView tvIncome;
    @BindView(R.id.tv_cost)
    TextView tvCost;

    private BaseApplication app;
    private List<Category> categories = new ArrayList<>();
    private Calendar calendar;
    private EntryAddCategoryAdatper adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.entry_add_layout;
    }

    @Override
    protected void initAfter() {
        ButterKnife.bind(this);
        registerEventBus();
        initData();
        initView();
        switchType(Category.TYPE_INCOME);
    }

    private void initData() {
        app = (BaseApplication) getApplication();
        calendar = Calendar.getInstance(Locale.CHINA);
    }

    private void initView() {
        tvDate.setText(getDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        tvTime.setText(getTimeString(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        adapter = new EntryAddCategoryAdatper(this, categories);
        spinnerCategory.setAdapter(adapter);
    }

    private void initCategorySpinner(int type) {
        categories.clear();
        for (Category category : app.getCategories()) {
            if (category.getType() == type)
                categories.add(category);
        }
        adapter.notifyDataSetChanged();
    }

    private void switchType(int type) {
        switch (type) {
            case Category.TYPE_INCOME:
                tvIncome.setSelected(true);
                tvCost.setSelected(false);
                initCategorySpinner(Category.TYPE_INCOME);
                break;
            case Category.TYPE_COST:
                tvIncome.setSelected(false);
                tvCost.setSelected(true);
                initCategorySpinner(Category.TYPE_COST);
                break;
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_income, R.id.tv_cost, R.id.tv_category_manage, R.id.tv_date, R.id.tv_time, R.id.tv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_income:
                switchType(Category.TYPE_INCOME);
                break;
            case R.id.tv_cost:
                switchType(Category.TYPE_COST);
                break;
            case R.id.tv_category_manage:
                startActivity(new Intent(this, CategoryManageActivity.class));
                finish();
                break;
            case R.id.tv_date:
                new DatePickerDialog(this, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.tv_time:
                new TimePickerDialog(this, onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                break;
            case R.id.tv_add:
                String amount = etAmount.getText().toString();
                if (amount.isEmpty()) {
                    Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
                    break;
                } else if (amount.contains(".")) {
                    int position = amount.length() - amount.indexOf('.') - 1;
                    if (position > 2) {
                        Toast.makeText(this, "不能提交两位以上的小数", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (position == 0) {
                        amount = amount.substring(0, amount.length() - 1);
                    }
                }
                Log.d("amount", amount);
                String time = tvDate.getText().toString() + " " + tvTime.getText().toString();
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                int position = spinnerCategory.getSelectedItemPosition();
                if (position > adapter.getCount() - 1) {
                    position = adapter.getCount() - 1;
                }
                app.getDbManager().addEntry(new Entry(adapter.getItem(position).getId(), Float.valueOf(amount), time, calendar.getTimeInMillis(), etInfo.getText().toString().trim()));
                finish();
                break;
        }
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(year, monthOfYear, dayOfMonth);
            tvDate.setText(getDateString(year, monthOfYear + 1, dayOfMonth));
        }
    };
    private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            tvTime.setText(getTimeString(hourOfDay, minute));
        }
    };

    private CharSequence getDateString(int year, int month, int day) {
        return new StringBuilder().append(year).append("-").append(month < 10 ? "0" + month : month).append("-").append(day < 10 ? "0" + day : day);
    }

    private CharSequence getTimeString(int hour, int minute) {
        return new StringBuilder().append(hour < 10 ? "0" + hour : hour).append(":").append(minute < 10 ? "0" + minute : minute);
    }
}
