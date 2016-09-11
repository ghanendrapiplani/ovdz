package com.o2cinemas.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.o2cinemas.o2vidz.R;

import java.util.List;

/**
 * Created by user on 4/28/2016.
 */
public class NavigationAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    private int[] images = {R.drawable.my_down, R.drawable.my_fav, R.drawable.refresh, R.drawable.setting, R.drawable.feed,
            R.drawable.invite, R.drawable.fb, R.drawable.soft_update, R.drawable.about, R.drawable.exit};


    private String[] names;

    public NavigationAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        names =  context.getResources().getStringArray(R.array.nav_drawer_items);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.drawer_list_item, null);
            viewHolder.nav_text = (TextView) convertView.findViewById(R.id.nav_text);
            viewHolder.nav_icon = (ImageView) convertView.findViewById(R.id.nav_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nav_text.setText(names[position]);
        viewHolder.nav_icon.setBackgroundResource(images[position]);

        return convertView;
    }

    public class ViewHolder {
        TextView nav_text;
        ImageView nav_icon;
    }
}
