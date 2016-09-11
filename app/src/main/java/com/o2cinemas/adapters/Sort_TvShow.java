package com.o2cinemas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.o2cinemas.beans.SortTvShow_item;
import com.o2cinemas.o2vidz.R;

import java.util.List;

/**
 * Created by user on 6/10/2016.
 */
public class Sort_TvShow extends BaseAdapter {
    Context context;
    List<SortTvShow_item> sortTvShow_items;
    LayoutInflater layoutInflater = null;

    public Sort_TvShow(Context context, List<SortTvShow_item> sortTvShow_items) {
        this.context = context;
        this.sortTvShow_items = sortTvShow_items;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sortTvShow_items.size();
    }

    @Override
    public Object getItem(int position) {
        return sortTvShow_items.get(position);
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
        SortTvShow_item item = sortTvShow_items.get(position);
        holder.alpha.setText(item.getAlpha());

        return convertView;
    }

    private class GridHolder {
        TextView alpha;
    }
}
