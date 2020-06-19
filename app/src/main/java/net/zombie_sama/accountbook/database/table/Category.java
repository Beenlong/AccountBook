package net.zombie_sama.accountbook.database.table;

import android.support.annotation.Nullable;

public class Category {
    public static final int TYPE_INCOME = 0;
    public static final int TYPE_COST = 1;
    private int type;
    private int id;
    private String name;
    private String info;


    public Category(int id, String name, @Nullable String info, int type) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.type = type;
    }

    public Category(String name, @Nullable String info, int type) {
        this.name = name;
        this.info = info;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return name;
    }
}
