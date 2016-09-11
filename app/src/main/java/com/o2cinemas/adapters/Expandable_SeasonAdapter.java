package com.o2cinemas.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.o2cinemas.beans.Episode_Bean;
import com.o2cinemas.beans.SeasonBean;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.o2vidz.EpisodeDetail;
import com.o2cinemas.o2vidz.R;

import org.w3c.dom.Text;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static com.o2cinemas.utilities.ExpandableListHeight.setListViewHeightBasedOnChildren;

/**
 * Created by user on 5/31/2016.
 */
public class Expandable_SeasonAdapter extends BaseExpandableListAdapter {
    private ArrayList<SeasonBean> seasonBeanArrayList;
    private Context context;

    public Expandable_SeasonAdapter(ArrayList<SeasonBean> seasonBeanArrayList, Context context) {
        this.seasonBeanArrayList = seasonBeanArrayList;

        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return seasonBeanArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Episode_Bean> episode_beans = seasonBeanArrayList.get(groupPosition).getEpisode_beans();
        return episode_beans.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return seasonBeanArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Episode_Bean> episode_beans = seasonBeanArrayList.get(groupPosition).getEpisode_beans();
        return episode_beans.get(childPosition);

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.season_tv_layout, null);
        }

        TextView season_no = (TextView) convertView.findViewById(R.id.season_number);
        TextView totl_views = (TextView) convertView.findViewById(R.id.season_view);
        ImageView season_arrow = (ImageView) convertView.findViewById(R.id.season_arrow);
        TextView season_episode = (TextView) convertView.findViewById(R.id.season_episode);
//        season_arrow.setSelected(isExpanded);

//        Arrays.sort(a, Collections.reverseOrder());
        season_no.setText(seasonBeanArrayList.get(groupPosition).getSeason_name());
        Collections.sort(seasonBeanArrayList, new Comparator<SeasonBean>() {
            @Override
            public int compare(SeasonBean lhs, SeasonBean rhs) {

                return lhs.getSeason_name().compareTo(rhs.getSeason_name());

            }
        });
        Collections.reverse(seasonBeanArrayList);
        totl_views.setText(seasonBeanArrayList.get(groupPosition).getSeason_views());
        ArrayList<String> episodeList = new ArrayList<>();
        if (seasonBeanArrayList.get(groupPosition).getEpisode_beans().size() > 0){
            for (int i = 0; i<seasonBeanArrayList.get(groupPosition).getEpisode_beans().size() ; i++){
                episodeList.add("Episode" + (i+1));
            }
        } int episozeSize = episodeList.size();
        season_episode.setText("" +episozeSize +" Episodes ");
        return convertView;

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.episode_list, null);
        }

        TextView episode_no = (TextView) convertView.findViewById(R.id.episode_number);
        TextView episode_views_no = (TextView) convertView.findViewById(R.id.episode_view);
        LinearLayout episode_layout = (LinearLayout) convertView.findViewById(R.id.episodeDetail_layout);
//        Collections.sort(seasonBeanArrayList, new Comparator<SeasonBean>() {
//            @Override
//            public int compare(SeasonBean lhs, SeasonBean rhs) {
//                Log.e("episode", ":-" + lhs.getEpisode_beans().get(childPosition).getEpisode_name().substring(0, 10));
//
//                return lhs.getEpisode_beans().get(childPosition).getEpisode_views().compareTo(rhs.getEpisode_beans().get(childPosition).getEpisode_views());
//
//            }
//        });
//        Collections.reverse(seasonBeanArrayList.get(groupPosition).getEpisode_beans());
        episode_no.setText(seasonBeanArrayList.get(groupPosition).getEpisode_beans().get(childPosition).getEpisode_name());
        episode_views_no.setText(seasonBeanArrayList.get(groupPosition).getEpisode_beans().get(childPosition).getEpisode_views());
        episode_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String season_name = seasonBeanArrayList.get(groupPosition).getSeason_name();
                String episode_name = seasonBeanArrayList.get(groupPosition).getEpisode_beans().get(childPosition).getEpisode_name();
                String episode_id = seasonBeanArrayList.get(groupPosition).getEpisode_beans().get(childPosition).getEpisode_id();
                Intent intent = new Intent(context, EpisodeDetail.class);
                intent.putExtra(Constants.EPISODE_ID ,episode_id);
                intent.putExtra(Constants.SEASON_NAME,season_name);
                intent.putExtra(Constants.EPISODE_NAME , episode_name);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
