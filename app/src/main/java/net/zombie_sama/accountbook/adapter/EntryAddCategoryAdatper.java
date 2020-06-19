package net.zombie_sama.accountbook.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import net.zombie_sama.accountbook.R;
import net.zombie_sama.accountbook.database.table.Category;

import java.util.List;

public class EntryAddCategoryAdatper extends ArrayAdapter<Category> {
    public EntryAddCategoryAdatper(Context context, List<Category> objects) {
        super(context, android.R.layout.simple_spinner_item, objects);
        setDropDownViewResource(R.layout.spinner_item_layout);
    }
}
