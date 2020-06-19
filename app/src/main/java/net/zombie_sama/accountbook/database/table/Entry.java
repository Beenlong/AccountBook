package net.zombie_sama.accountbook.database.table;

import android.support.annotation.Nullable;

public class Entry {
    private int id;
    private int categoryId;
    private float amount;
    private String time;
    private long timestamp;
    private String info;


    public Entry(int id, int categoryId, float amount, String time, long timestamp, @Nullable String info) {
        this.id = id;
        this.categoryId = categoryId;
        this.amount = amount;
        this.time = time;
        this.timestamp = timestamp;
        this.info = info;
    }

    public Entry(int categoryId, float amount, String time, long timestamp, @Nullable String info) {
        this.categoryId = categoryId;
        this.amount = amount;
        this.time = time;
        this.timestamp = timestamp;
        this.info = info;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}