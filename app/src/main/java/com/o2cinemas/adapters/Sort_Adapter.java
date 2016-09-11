package com.o2cinemas.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.o2cinemas.beans.SortTvShow_item;
import com.o2cinemas.o2vidz.R;

import java.util.List;

/**
 * Created by user on 6/14/2016.
 */
public class Sort_Adapter extends RecyclerView.Adapter<Sort_Adapter.SortHolder> {
    private Context context;
    List<SortTvShow_item> sortTvShow_items;

    public Sort_Adapter(Context context, List<SortTvShow_item> sortTvShow_items) {
        this.context = context;
        this.sortTvShow_items = sortTvShow_items;
    }

    @Override
    public SortHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sort_tvshow, parent, false);
        SortHolder viewHolder = new SortHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SortHolder holder, int position) {
        SortTvShow_item item = sortTvShow_items.get(position);
        holder.sortAplha.setTextScaleX(View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        holder.sortAplha.setText(item.getAlpha());
    }



    @Override
    public int getItemCount() {
        return sortTvShow_items.size();
    }
   class SortHolder extends RecyclerView.ViewHolder {
private TextView sortAplha;
       public SortHolder(View itemView) {
           super(itemView);

           sortAplha = (TextView) itemView.findViewById(R.id.sort_alpha);
       }


   }
}
