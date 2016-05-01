package net.zombie_sama.accountbook.base;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import org.simple.eventbus.EventBus;

public class BaseApplication extends Application {
    private SQLiteDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

}
