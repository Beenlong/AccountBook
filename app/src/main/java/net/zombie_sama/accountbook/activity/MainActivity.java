package net.zombie_sama.accountbook.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.zombie_sama.accountbook.R;
import net.zombie_sama.accountbook.adapter.MainPageEntryAdapter;
import net.zombie_sama.accountbook.base.BaseActivity;
import net.zombie_sama.accountbook.base.BaseApplication;
import net.zombie_sama.accountbook.database.table.Category;
import net.zombie_sama.accountbook.database.table.Entry;
import net.zombie_sama.accountbook.dialog.MonthPicker;
import net.zombie_sama.accountbook.dialog.YMPickerDialog;
import net.zombie_sama.accountbook.event.EntryUpdateEvent;
import net.zombie_sama.accountbook.util.Util;

import org.simple.eventbus.Subscriber;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_income)
    TextView tvIncome;
    @BindView(R.id.tv_cost)
    TextView tvCost;
    @BindView(R.id.tv_remain)
    TextView tvRemain;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.lv_recent_entry)
    ListView lvRecentEntry;

    private BaseApplication app;
    private List<Entry> entries;
    private MainPageEntryAdapter adapter;
    private YMPickerDialog picker;
    private Calendar calendar;
    private float sum_income;
    private float sum_cost;
    private float sum_total;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initAfter() {
        ButterKnife.bind(this);
        registerEventBus();
        initData();
        initView();
        Util.showUpToDateDialog(this);
    }

    private void initData() {
        app = BaseApplication.getInstance();
        entries = app.getEntries();
    }

    private void initView() {
        calculate();
        adapter = new MainPageEntryAdapter(this, entries);
        lvRecentEntry.setAdapter(adapter);
        lvRecentEntry.setOnItemClickListener(this);
        lvRecentEntry.setOnItemLongClickListener(this);
        setDate(app.getCalendar());
    }

    private void setDate(Calendar calendar) {
        this.calendar = calendar;
        String date = calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月";
        tvDate.setText(date);
    }

    public void calculate() {
        sum_income = 0;
        sum_cost = 0;
        for (Entry entry : entries) {
            if (Category.TYPE_INCOME == app.getDbManager().queryCategoryFromChild(entry).getType())
                sum_income += entry.getAmount();
            else
                sum_cost += entry.getAmount();
        }
        sum_total = sum_income - sum_cost;
        tvIncome.setText(String.valueOf(new DecimalFormat("0.00").format(sum_income)));
        tvCost.setText(String.valueOf(new DecimalFormat("0.00").format(sum_cost)));
        tvRemain.setText(String.valueOf(new DecimalFormat("0.00").format(sum_total)));
    }

    @Subscriber
    private void onEntryUpdate(EntryUpdateEvent event) {
        adapter.notifyDataSetChanged();
        setDate(event.getCalendar());
        calculate();
    }

    @OnClick({R.id.tv_date, R.id.tv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date:
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                MonthPicker.showPicker(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, new MonthPicker.DateSetListener() {
                    @Override
                    public void onDateSet(int year, int month) {
                        Calendar c = Calendar.getInstance();
                        c.set(year, month, 0);
                        app.queryEntry(c);
                    }
                });
                break;
            case R.id.tv_add:
                startActivity(new Intent(this, EntryAddActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Entry entry = entries.get(position);
        String info = entry.getInfo();
        if (null != info && !info.isEmpty())
            Toast.makeText(this, entry.getInfo(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "这条记录没有添加备注", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(this).setTitle("删除该条目？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        app.getDbManager().removeEntry(entries.get(position));
                    }
                }).setNegativeButton("取消", null).show();
        return true;
    }
}
