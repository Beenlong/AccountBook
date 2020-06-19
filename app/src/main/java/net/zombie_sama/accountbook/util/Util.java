package net.zombie_sama.accountbook.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;

public class Util {
    public static void showUpToDateDialog(Context context) {
        // TODO: 2017/6/1 改为网络请求
        int version = 1;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (PreferencesManager.getInstance().isUpToDate(version)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("版本更新");
            builder.setMessage("1.主页修改为按月查询记录\n2.移除了类别分组，只保留分类\n");
            builder.setNegativeButton("我知道了……", null);
//            builder.show();
            PreferencesManager.getInstance().setVersion(version);
        }
    }
}
