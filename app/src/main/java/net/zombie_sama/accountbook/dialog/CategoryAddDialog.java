package net.zombie_sama.accountbook.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.zombie_sama.accountbook.R;
import net.zombie_sama.accountbook.base.BaseApplication;
import net.zombie_sama.accountbook.database.table.Category;

public class CategoryAddDialog {
    private static EditText etName;
    private static EditText etInfo;
    private static AlertDialog dialog;
    private static RadioGroup radioGroup;
    private static int type = Category.TYPE_INCOME;

    public static AlertDialog create(final Context context) {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.add_category_dialog, null);
            etName = (EditText) view.findViewById(R.id.et_name);
            etInfo = (EditText) view.findViewById(R.id.et_info);
            radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.rb_income:
                            type = Category.TYPE_INCOME;
                            break;
                        case R.id.rb_cost:
                            type = Category.TYPE_COST;
                            break;
                    }
                }
            });
            radioGroup.check(R.id.rb_income);
            builder.setTitle(R.string.category_add_dialog_title);
            builder.setView(view);
            builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface iDialog, int which) {
                    dialog = null;
                    if (etName.getText().toString().isEmpty()) {
                        Toast.makeText(context, "分类名不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ((BaseApplication) context.getApplicationContext())
                            .getDbManager()
                            .addCategory(new Category(
                                    etName.getText().toString(),
                                    etInfo.getText().toString(), type));
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface iDialog, int which) {
                    dialog = null;
                }
            });
            dialog = builder.create();
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog = null;
                }
            });
        }
        return dialog;
    }
}
