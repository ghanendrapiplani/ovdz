package com.o2cinemas.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Created by user on 6/21/2016.
 */
public class SeasonAdapter  extends RecyclerView.Adapter<SeasonAdapter.ItemViewHolder> {

    private ArrayList<HomeMoivesBean> beanArrayList;
    private Context context;
    private ImageLoader imageLoader;

    public SeasonAdapter(ArrayList<HomeMoivesBean> beanArrayList, Context context
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
        holder.Text_mp4.setText(beanArrayList.get(position).getTitle());
        String type = beanArrayList.get(position).getType();
        String imageUrl = "";
        imageUrl = beanArrayList.get(position).getImage();
//        if (type.equalsIgnoreCase("mp4Movies")) {
//            imageUrl = Constants.imageResponseMp4 + imageUrl.replace(" ", "%20");
//            loadMoiveImage(imageUrl, holder.Image_mp4);
//        } else if (type.equalsIgnoreCase("3gpMovies")) {
//            imageUrl = Constants.imageResponseGp3 + imageUrl.replace(" ", "%20");
//            loadMoiveImage(imageUrl, holder.Image_mp4);
//
//        } else if (type.equalsIgnoreCase("tvshows")) {
            imageUrl = Constants.imageResponseTvShow + imageUrl.replace(" ", "%20");
            loadMoiveImage(imageUrl, holder.Image_mp4);


//Log.e("title",":-"+beanArrayList);
    }

    private void loadMoiveImage(String imageUrl, ImageView image_mp4) {

        imageLoader.get(imageUrl, ImageLoader.getImageListener(image_mp4, R.drawable.error, R.drawable.error));

    }

    @Override
    public int getItemCount() {
        return beanArrayList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView Image_mp4;
        private TextView Text_mp4;

        public ItemViewHolder(View itemView) {
            super(itemView);

            Image_mp4 = (ImageView) itemView.findViewById(R.id.Image_mp4);
            Text_mp4 = (TextView) itemView.findViewById(R.id.Text_mp4);
            Text_mp4.setOnClickListener(this);
            Image_mp4.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.Text_mp4:
                    String type = beanArrayList.get(getAdapterPosition()).getType();
//                    if (type.equalsIgnoreCase("mp4Movies") || type.equalsIgnoreCase("3gpMovies")) {
//                        String image = beanArrayList.get(getAdapterPosition()).getImage();
//                        String id = beanArrayList.get(getAdapterPosition()).getId();
//                        Intent intent = new Intent(context, Detail_Page.class);
//                        intent.putExtra(Constants.MOVIE_ID, id);
//                        intent.putExtra(Constants.MOVIE_TYPE, type);
//                        intent.putExtra(Constants.MOVIE_IMAGE, image);
//                        context.startActivity(intent);
//                    } else if (type.equalsIgnoreCase("tvshows")) {
                        String image = beanArrayList.get(getAdapterPosition()).getImage();
                        String id = beanArrayList.get(getAdapterPosition()).getId();
                        Intent intent1 = new Intent(context, TvShow_SeasonDetail.class);
                        intent1.putExtra(Constants.SHOW_ID, id);
                        intent1.putExtra(Constants.SHOW_TYPE, type);
                        intent1.putExtra(Constants.SHOW_IMAGE, image);
                        context.startActivity(intent1);

                    break;

                case R.id.Image_mp4:
                    String type1 = beanArrayList.get(getAdapterPosition()).getType();
//                    if (type1.equalsIgnoreCase("mp4Movies") || type1.equalsIgnoreCase("3gpMovies")) {
//                        String image = beanArrayList.get(getAdapterPosition()).getImage();
//                        String id = beanArrayList.get(getAdapterPosition()).getId();
//                        Intent intent = new Intent(context, Detail_Page.class);
//                        intent.putExtra(Constants.MOVIE_ID, id);
//                        intent.putExtra(Constants.MOVIE_TYPE, type1);
//                        intent.putExtra(Constants.MOVIE_IMAGE, image);
//                        context.startActivity(intent);
//                    } else if (type1.equalsIgnoreCase("tvshows")) {
                        String image1 = beanArrayList.get(getAdapterPosition()).getImage();
                        String id1 = beanArrayList.get(getAdapterPosition()).getId();
                        Intent intent2 = new Intent(context, TvShow_SeasonDetail.class);
                        intent2.putExtra(Constants.SHOW_ID, id1);
                        intent2.putExtra(Constants.SHOW_TYPE, type1);
                        intent2.putExtra(Constants.SHOW_IMAGE, image1);
                        context.startActivity(intent2);


                        break;
                    }
            }
        }
    }
