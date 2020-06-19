package net.zombie_sama.accountbook.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.zombie_sama.accountbook.R;
import net.zombie_sama.accountbook.adapter.CategoryManageCategoryAdapter;
import net.zombie_sama.accountbook.base.BaseActivity;
import net.zombie_sama.accountbook.base.BaseApplication;
import net.zombie_sama.accountbook.database.table.Category;
import net.zombie_sama.accountbook.dialog.CategoryAddDialog;
import net.zombie_sama.accountbook.event.DataUpdateEvent;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryManageActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_add_category)
    TextView tvAddCategory;
    @BindView(R.id.lv_category)
    ListView lvCategory;

    private BaseApplication app;
    private List<Category> categories = new ArrayList<>();
    private CategoryManageCategoryAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.category_manage_layout;
    }

    @Override
    protected void initAfter() {
        ButterKnife.bind(this);
        registerEventBus();
        initData();
        initView();
    }

    private void initData() {
        app = (BaseApplication) getApplication();
        categories = app.getCategories();
    }

    private void initView() {

        adapter = new CategoryManageCategoryAdapter(this, categories);
        lvCategory.setAdapter(adapter);
        lvCategory.setOnItemClickListener(onCategoryItemClick);
        lvCategory.setOnItemLongClickListener(onCategoryItemLongClick);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, EntryAddActivity.class));
        finish();
    }

    private AdapterView.OnItemClickListener onCategoryItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(CategoryManageActivity.this, "分类:" + adapter.getItem(position).getName() + ",备注:" + adapter.getItem(position).getInfo(), Toast.LENGTH_SHORT).show();
        }
    };

    private AdapterView.OnItemLongClickListener onCategoryItemLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            TextView tv = new TextView(CategoryManageActivity.this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tv.setLayoutParams(params);
            tv.setGravity(Gravity.CENTER);
            tv.setText("这将同时删除该分类下的所有条目");
            new AlertDialog.Builder(CategoryManageActivity.this).setTitle("删除该分类？").setView(tv)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            app.getDbManager().removeCategory(adapter.getItem(position));
                        }
                    }).setNegativeButton("取消", null).show();
            return true;
        }
    };

    @Subscriber
    private void onDataUpdate(DataUpdateEvent event) {
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_back, R.id.tv_add_category})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_add_category:
                CategoryAddDialog.create(this).show();
                break;
        }
    }
}
