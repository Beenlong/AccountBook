package net.zombie_sama.accountbook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import net.zombie_sama.accountbook.database.table.Category;
import net.zombie_sama.accountbook.database.table.Entry;
import net.zombie_sama.accountbook.event.CategoryChangedEvent;
import net.zombie_sama.accountbook.event.EntryChangedEvent;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DatabaseManager {
    private SQLiteDatabase db;
    private DatabaseOpenHelper helper;
    private Context context;

    public DatabaseManager(Context context) {
        this.context = context;
        helper = new DatabaseOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 增加类别
     *
     * @param category
     * @return 是否操作成功
     */
    public boolean addCategory(Category category) {
        boolean isSuccess;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("name", category.getName());
            values.put("info", category.getInfo());
            values.put("type", category.getType());
            db.insert(DatabaseOpenHelper.TABLE_CATEGORY, null, values);
            db.setTransactionSuccessful();
            isSuccess = true;
            EventBus.getDefault().post(new CategoryChangedEvent());
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }


    /**
     * 增加流水记录
     *
     * @param entry
     * @return 是否操作成功
     */
    public boolean addEntry(Entry entry) {
        boolean isSuccess;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("category_id", entry.getCategoryId());
            values.put("amount", entry.getAmount());
            values.put("time", entry.getTime());
            values.put("timestamp", entry.getTimestamp());
            values.put("info", entry.getInfo());
            db.insert(DatabaseOpenHelper.TABLE_ENTRY, null, values);
            db.setTransactionSuccessful();
            isSuccess = true;
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.setTimeInMillis(entry.getTimestamp());
            EventBus.getDefault().post(new EntryChangedEvent(calendar));
            Log.d("添加流水", "成功");
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }

    /**
     * 修改流水条目
     *
     * @param entry
     * @return 是否操作成功
     */
    public boolean updateEntry(Entry entry) {
        boolean isSuccess;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("categoryId", entry.getCategoryId());
            values.put("amount", entry.getAmount());
            values.put("time", entry.getTime());
            values.put("timestamp", entry.getTimestamp());
            values.put("info", entry.getInfo());
            db.update(DatabaseOpenHelper.TABLE_ENTRY, values, "id = ?", new String[]{String.valueOf(entry.getId())});
            db.setTransactionSuccessful();
            isSuccess = true;
//            Calendar calendar = Calendar.getInstance(Locale.CHINA);
//            calendar.setTimeInMillis(entry.getTimestamp());
//            EventBus.getDefault().post(new EntryChangedEvent(calendar));
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }

    /**
     * 移除流水条目
     *
     * @param entry
     * @return 是否操作成功
     */
    public boolean removeEntry(Entry entry) {
        boolean isSuccess;
        db.beginTransaction();
        try {
            db.delete(DatabaseOpenHelper.TABLE_ENTRY, "id = ?", new String[]{String.valueOf(entry.getId())});
            db.setTransactionSuccessful();
            isSuccess = true;
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.setTimeInMillis(entry.getTimestamp());
            EventBus.getDefault().post(new EntryChangedEvent(calendar));
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }

    /**
     * 移除类别下的所有条目
     *
     * @param category
     * @return 是否操作成功
     */
    public boolean removeEntriesInCategory(Category category) {
        boolean isSuccess;
        int count;
        db.beginTransaction();
        try {
            count = db.delete(DatabaseOpenHelper.TABLE_ENTRY, "category_id = ?", new String[]{String.valueOf(category.getId())});
            db.setTransactionSuccessful();
            isSuccess = true;
            Log.d("删除分类中的流水记录", "删除了 " + count + " 条记录");
            EventBus.getDefault().post(new EntryChangedEvent(Calendar.getInstance(Locale.CHINA)));
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }

    /**
     * 移除类别
     *
     * @param category
     * @return 是否操作成功
     */
    public boolean removeCategory(Category category) {
        boolean isSuccess;
        if (!removeEntriesInCategory(category)) {
            Toast.makeText(context, "删除分类下的条目失败", Toast.LENGTH_SHORT).show();
            return false;
        }
        db.beginTransaction();
        try {
            db.delete(DatabaseOpenHelper.TABLE_CATEGORY, "id = ?", new String[]{String.valueOf(category.getId())});
            db.setTransactionSuccessful();
            isSuccess = true;
            EventBus.getDefault().post(new CategoryChangedEvent());
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }

    /**
     * 查询所有分类
     *
     * @return 分类List
     */
    public List<Category> queryCategory() {
        ArrayList<Category> categories = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + DatabaseOpenHelper.TABLE_CATEGORY, null);
        while (cursor.moveToNext()) {
            Category category = new Category(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("info")),
                    cursor.getInt(cursor.getColumnIndex("type")));
            categories.add(category);
        }
        cursor.close();
        return categories;
    }

    /**
     * 查询所有流水条目
     *
     * @return 流水List
     */
    public List<Entry> queryEntry() {
        ArrayList<Entry> entries = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + DatabaseOpenHelper.TABLE_ENTRY, null);
        while (cursor.moveToNext()) {
            Entry entry = new Entry(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("category_id")),
                    cursor.getFloat(cursor.getColumnIndex("amount")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getLong(cursor.getColumnIndex("timestamp")),
                    cursor.getString(cursor.getColumnIndex("info")));
            entries.add(entry);
        }
        cursor.close();
        Collections.reverse(entries);
        return entries;
    }

    /**
     * 按月查询流水条目
     *
     * @return 流水List
     */
    public List<Entry> queryEntryByMonth(int year, int month) {
        ArrayList<Entry> entries = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance(Locale.CHINA);
        startCalendar.set(year, month, 1, 0, 0, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        Calendar endCalendar = Calendar.getInstance(Locale.CHINA);
        endCalendar.set(year, month + 1, 1, 0, 0, 0);
        endCalendar.set(Calendar.MILLISECOND, -1);
        Log.i("queryEntryByMonth", "select * from "
                + DatabaseOpenHelper.TABLE_ENTRY
                + " where timestamp between "
                + startCalendar.getTimeInMillis()
                + " and "
                + endCalendar.getTimeInMillis());
        Cursor cursor = db.rawQuery("select * from entry where timestamp between ? and ?",
                new String[]{String.valueOf(startCalendar.getTimeInMillis()), String.valueOf(endCalendar.getTimeInMillis())});

        while (cursor.moveToNext()) {
            Entry entry = new Entry(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("category_id")),
                    cursor.getFloat(cursor.getColumnIndex("amount")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getLong(cursor.getColumnIndex("timestamp")),
                    cursor.getString(cursor.getColumnIndex("info")));
            entries.add(entry);
        }
        cursor.close();
        Collections.reverse(entries);
        return entries;
    }

    public Category queryCategoryFromChild(@NonNull Entry entry) {
        Cursor cursor = db.rawQuery("select * from " + DatabaseOpenHelper.TABLE_CATEGORY + " where id = ?", new String[]{String.valueOf(entry.getCategoryId())});
        Category category = null;
        if (cursor.moveToNext()) {
            category = new Category(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("info")),
                    cursor.getInt(cursor.getColumnIndex("type")));
        }
        cursor.close();
        return category;
    }


    public void close() {
        helper.close();
    }
}
