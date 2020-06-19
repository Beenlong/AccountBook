package net.zombie_sama.accountbook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.zombie_sama.accountbook.R;
import net.zombie_sama.accountbook.base.BaseApplication;
import net.zombie_sama.accountbook.database.table.Category;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_CATEGORY = "category";
    public static final String TABLE_ENTRY = "entry";
    private static final String DATABASE_NAME = "account.db";
    private static final int DATABASE_VERSION = 3;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR not null, info TEXT, type INTEGER not null)");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ENTRY + "(id INTEGER PRIMARY KEY AUTOINCREMENT, category_id INTEGER not null, amount REAL not null, time TEXT, timestamp REAL, info TEXT)");
            initCategory(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            Log.d("数据库初始化", "完成");
        }
    }

    private void initCategory(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.clear();
        values.put("type", Category.TYPE_INCOME);
        values.put("name", "基础工资");
        db.insert(TABLE_CATEGORY, "无", values);

        values.clear();
        values.put("type", Category.TYPE_INCOME);
        values.put("name", "加班");
        db.insert(TABLE_CATEGORY, "无", values);

        values.clear();
        values.put("type", Category.TYPE_INCOME);
        values.put("name", "补贴");
        db.insert(TABLE_CATEGORY, "无", values);

        values.clear();
        values.put("type", Category.TYPE_COST);
        values.put("name", "吃饭");
        db.insert(TABLE_CATEGORY, "无", values);

        values.clear();
        values.put("type", Category.TYPE_COST);
        values.put("name", "交通");
        db.insert(TABLE_CATEGORY, "无", values);

        values.clear();
        values.put("type", Category.TYPE_COST);
        values.put("name", "房租");
        db.insert(TABLE_CATEGORY, "无", values);

        values.clear();
        values.put("type", Category.TYPE_COST);
        values.put("name", "游戏花费");
        db.insert(TABLE_CATEGORY, "无", values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.beginTransaction();
            try {
                db.execSQL("ALTER TABLE " + TABLE_ENTRY + " ADD timestamp REAL");
                SimpleDateFormat format = new SimpleDateFormat(BaseApplication.getInstance().getString(R.string.pattern));
                Cursor cursor = db.rawQuery("select * from " + DatabaseOpenHelper.TABLE_ENTRY, null);
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    long timestamp = format.parse(time).getTime();
                    ContentValues values = new ContentValues();
                    values.put("timestamp", timestamp);
                    db.update(TABLE_ENTRY, values, "id = ?", new String[]{String.valueOf(id)});
                }
                cursor.close();
                db.setTransactionSuccessful();
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }

        if (oldVersion < 3) {
            db.beginTransaction();
            try {
                Cursor cursor = db.rawQuery("select id,type from category_group", null);
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));
                    ContentValues values = new ContentValues();
                    values.put("super_id", type);
                    db.update(TABLE_CATEGORY, values, "super_id = ?", new String[]{String.valueOf(id)});
                }
                cursor.close();
                db.execSQL("ALTER TABLE category RENAME TO category_b");
                db.execSQL("CREATE TABLE category(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR not null, info TEXT, type INTEGER not null)");
                db.execSQL("INSERT INTO category SELECT id,name,info,super_id FROM category_b");
                db.execSQL("DROP TABLE category_b");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }
}
