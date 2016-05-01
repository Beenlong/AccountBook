package net.zombie_sama.accountbook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "account.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS category_main(id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER not null, name VARCHAR not null, info TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS category_sub(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR not null, info TEXT, super_id INTEGER not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
