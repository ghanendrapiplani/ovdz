package com.o2cinemas.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.o2cinemas.beans.Detail_Bean;
import com.o2cinemas.constants.Constants;
import com.o2cinemas.models.PartDetails;
import com.o2cinemas.o2vidz.Detail_Page;
import com.o2cinemas.o2vidz.MainActivity;
import com.o2cinemas.o2vidz.R;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by user on 5/27/2016.
 */
public class Detail_Adapter extends RecyclerView.Adapter<Detail_Adapter.DetailHolder> {
    private Context context;
    private ArrayList<PartDetails> detail_bean;
    private RadioButton lastChecked = null;
//    private boolean isSelected[] = {true, false, false};
//    private CheckBox checkBox;

    public Detail_Adapter(Context context, ArrayList<PartDetails> detail_bean) {
        this.context = context;
        this.detail_bean = detail_bean;

    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.download_list, parent, false);
        DetailHolder detailHolder = new DetailHolder(view);
        return detailHolder;
    }

    @Override
    public void onBindViewHolder(final DetailHolder holder, final int position) {

        holder.setPartDetails(detail_bean.get(position));
        holder.list_name.setText(detail_bean.get(position).getPartName());
//        holder.linearLayout.setSelected(selectedItems.get(position, false));

    }


//

    @Override
    public int getItemCount() {
        return detail_bean.size();
    }


    public class DetailHolder extends RecyclerView.ViewHolder {
        private ImageView checkbox;
        private TextView list_name;
        private LinearLayout linearLayout;
        private RadioGroup radioGroup;
        private PartDetails partDetails;

        public DetailHolder(View itemView) {
            super(itemView);
            list_name = (TextView) itemView.findViewById(R.id.list_text);
            checkbox = (ImageView) itemView.findViewById(R.id.list_checkbox);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear);
//            radioGroup= (RadioGroup) itemView.findViewById(R.id.rg);
//            radioGroup.clearCheck();

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (partDetails != null) {
                        if (!partDetails.isSelected()) {
                            checkbox.setImageResource(R.drawable.selected);
                            linearLayout.setSelected(false);
//                            ((Detail_Page) context).getCurrentPositionofList(getAdapterPosition());
                            setSelected(partDetails);
                        } else {
                            checkbox.setImageResource(R.drawable.un_selected);
                            linearLayout.setSelected(true);
                            partDetails.setSelected(false);
                        }

                        notifyDataSetChanged();

//                    for(int i=0; i < isSelected.length; i++){
//                        if (i==getAdapterPosition()){
//                            isSelected[getAdapterPosition()]=true;}
//                            else{
//                            isSelected[getAdapterPosition()]=false;
//                        }
//                        notifyDataSetChanged();
//                    }
//
//
//                if (isSelected[getAdapterPosition()]==true){
//                    //execute your codes here
//                    checkbox.setImageResource(R.drawable.selected);
//                }else{
//                    //execute your codes here
//                    checkbox.setImageResource(R.drawable.un_selected);
//                }
                    }
                }
            });
//            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                    RadioButton chk = (RadioButton) group.findViewById(R.id.list_checkbox);
//                if (lastChecked!= null && checkedId > -1){
//                        lastChecked.setChecked(false);
//                        chk.setBackgroundResource(R.drawable.selected);
//                        Toast.makeText(context,"bdhjfb",Toast.LENGTH_SHORT).show();
//                    }
//                    lastChecked = chk;
//                    chk.setBackgroundResource(R.drawable.un_selected);
//                }
//            });
        }

        public void setPartDetails(PartDetails partDetails) {
            this.partDetails = partDetails;
            if (partDetails != null) {
                if (partDetails.isSelected()) {
                    checkbox.setImageResource(R.drawable.selected);
                    linearLayout.setSelected(false);
                } else {
                    checkbox.setImageResource(R.drawable.un_selected);
                    linearLayout.setSelected(true);
                }
            }
        }
    }

    private void setSelected(PartDetails partDetails) {
        if (partDetails != null) {
            for (PartDetails tmp : detail_bean) {
                String partDetailsName = partDetails.getPartName();
                String tmpName = tmp.getPartName();
                if (partDetailsName!= null && tmpName != null
                        && partDetailsName.equals(tmpName)) {
                    tmp.setSelected(true);
                    partDetails.setSelected(true);
                } else {
                    tmp.setSelected(false);
                }
            }
        }

    }

    public PartDetails getSelectedItem() {
        for (PartDetails partDetails : detail_bean) {
            if (partDetails.isSelected()) {
                return partDetails;
            }
        }

        return null;
    }

}
