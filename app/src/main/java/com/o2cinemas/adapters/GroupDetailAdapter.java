package com.o2cinemas.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.o2cinemas.beans.HomeMoivesBean;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.o2vidz.Detail_Page;
import com.o2cinemas.o2vidz.EpisodeDetail;
import com.o2cinemas.o2vidz.R;
import com.o2cinemas.o2vidz.TvShow_SeasonDetail;
import com.o2cinemas.volley.AppController;

import java.util.ArrayList;

/**
 * Created by admin on 9/9/16.
 */
public class GroupDetailAdapter extends RecyclerView.Adapter<GroupDetailAdapter.ItemViewHolder> {
    private ArrayList<HomeMoivesBean> beanArrayList;
    private Context context;
    private ImageLoader imageLoader;

    public GroupDetailAdapter(ArrayList<HomeMoivesBean> beanArrayList, Context context
    ) {
        this.beanArrayList = beanArrayList;
        this.context = context;
        this.imageLoader = AppController.getInstance().getImageLoader();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.mp4_grid_adapter, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bindData(beanArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return beanArrayList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView Image_mp4;
        private TextView Text_mp4;
        private HomeMoivesBean item;

        public ItemViewHolder(View itemView) {
            super(itemView);

            Image_mp4 = (ImageView) itemView.findViewById(R.id.Image_mp4);
            Text_mp4 = (TextView) itemView.findViewById(R.id.Text_mp4);
            Text_mp4.setOnClickListener(this);
            Image_mp4.setOnClickListener(this);
        }

        private void bindData(HomeMoivesBean item) {
            this.item = item;
            Text_mp4.setText(item.getTitle());
            String type = item.getType();
            String imageUrl = "";
            imageUrl = item.getImage();

            // select server base on type
            if (type.equalsIgnoreCase(Constants.MP_4_TYPE)) {
                imageUrl = Constants.imageResponseMp4 + imageUrl.replace(" ", "%20");
            } else if (type.equalsIgnoreCase(Constants.GP3_TYPE)) {
                imageUrl = Constants.imageResponseGp3 + imageUrl.replace(" ", "%20");

            } else if (type.equalsIgnoreCase(Constants.TV_SHOW_TYPE)) {
                imageUrl = Constants.imageResponseTvShow + imageUrl.replace(" ", "%20");
            }

            imageLoader.get(imageUrl, ImageLoader.getImageListener(Image_mp4, R.drawable.error, R.drawable.error));
        }
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.Text_mp4:

                    break;

                case R.id.Image_mp4:
                    String type = item.getType();
                    if (type.equalsIgnoreCase("mp4Movies") || type.equalsIgnoreCase("3gpMovies")) {
                        String image = item.getImage();
                        String id = item.getId();
                        Intent intent = new Intent(context, Detail_Page.class);
                        intent.putExtra(Constants.MOVIE_ID, id);
                        intent.putExtra(Constants.MOVIE_TYPE, type);
                        intent.putExtra(Constants.MOVIE_IMAGE, image);
                        context.startActivity(intent);
                    } else if (type.equalsIgnoreCase("tvshows")) {
                        String image1 = item.getImage();
                        String id1 = item.getId();
                        Intent intent2 = new Intent(context, EpisodeDetail.class);
                        intent2.putExtra(Constants.EPISODE_ID, id1);
                        intent2.putExtra(Constants.SHOW_TYPE, type);
                        intent2.putExtra(Constants.SHOW_IMAGE, image1);
                        context.startActivity(intent2);
                    }
                    break;
            }
        }
    }
}
