package net.zombie_sama.accountbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.zombie_sama.accountbook.R;
import net.zombie_sama.accountbook.database.table.Category;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryManageCategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> categories;

    public CategoryManageCategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.category_manage_categorylist_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvNum.setText(String.valueOf(position + 1));
        holder.tvName.setText(getItem(position).getName());
        holder.tvInfo.setText(getItem(position).getInfo());
        String type = "";
        switch (getItem(position).getType()) {
            case Category.TYPE_INCOME:
                type = "收入";
                break;
            case Category.TYPE_COST:
                type = "支出";
                break;
        }
        holder.tvType.setText(type);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.tv_type)
        TextView tvType;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
