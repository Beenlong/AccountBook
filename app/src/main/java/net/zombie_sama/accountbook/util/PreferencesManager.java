package net.zombie_sama.accountbook.util;


import android.content.Context;
import android.content.SharedPreferences;

import net.zombie_sama.accountbook.base.BaseApplication;

public class PreferencesManager {
    private static PreferencesManager instance;
    private SharedPreferences sp;

    public PreferencesManager() {
        sp = BaseApplication.getInstance().getSharedPreferences("account_book", Context.MODE_PRIVATE);
    }

    public static PreferencesManager getInstance() {
        if (instance == null)
            instance = new PreferencesManager();
        return instance;
    }

    public boolean isUpToDate(int version) {
        return sp.getInt("version", 0) != version;
    }

    public void setVersion(int version) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("version", version);
        edit.apply();
    }
}
