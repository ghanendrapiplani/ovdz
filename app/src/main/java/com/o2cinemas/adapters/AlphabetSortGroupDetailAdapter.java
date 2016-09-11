package com.o2cinemas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.o2cinemas.beans.SortTvShow_item;
import com.o2cinemas.models.AlphabetSortItem;
import com.o2cinemas.o2vidz.R;

import java.util.List;

/**
 * Created by admin on 9/10/16.
 */
public class AlphabetSortGroupDetailAdapter extends BaseAdapter {
    Context context;
    List<AlphabetSortItem> sortItems;
    LayoutInflater layoutInflater = null;

    public AlphabetSortGroupDetailAdapter(Context context, List<AlphabetSortItem> sortItems) {
        this.context = context;
        this.sortItems = sortItems;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sortItems.size();
    }

    @Override
    public Object getItem(int position) {
        return sortItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridHolder holder;
        if (convertView == null) {

            holder = new GridHolder();
            convertView = layoutInflater.inflate(R.layout.sort_tvshow, null);
            holder.alpha = (TextView) convertView.findViewById(R.id.sort_alpha);
            convertView.setTag(holder);
        } else {
            holder = (GridHolder) convertView.getTag();
        }

        AlphabetSortItem item = sortItems.get(position);

        if (item.isSelected()) {
            holder.alpha.setTextColor(context.getResources().getColor(android.R.color.black));
        } else {
            holder.alpha.setTextColor(context.getResources().getColor(android.R.color.white));
        }

        holder.alpha.setText(item.getValue());

        return convertView;
    }

    public void setSelectedItem(AlphabetSortItem selectedItem) {
        if (sortItems != null && sortItems.size() > 0) {
            for (AlphabetSortItem item:sortItems) {
                if (item.getValue().equals(selectedItem.getValue())){
                    item.setSelected(true);
                } else
                {
                    item.setSelected(false);
                }
            }
            notifyDataSetChanged();
        }
    }

    public AlphabetSortItem getSelectedItem() {
        if (sortItems != null && sortItems.size() > 0) {
            for (AlphabetSortItem item:sortItems) {
                if (item.isSelected()) {
                    return item;
                }
            }
        }

        return null;
    }

    private class GridHolder {
        TextView alpha;
    }
}