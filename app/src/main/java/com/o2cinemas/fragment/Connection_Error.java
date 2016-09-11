package com.o2cinemas.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.o2cinemas.o2vidz.MainActivity;
import com.o2cinemas.o2vidz.R;
import com.o2cinemas.utilities.ConnectionDetector;

/**
 * Created by user on 5/20/2016.
 */
public class Connection_Error extends Fragment {
    private TextView connection_error;
    private Button retry;
    private ConnectionDetector connectionDetector;
    private Context context;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.connection_error, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        connection_error = (TextView) getView().findViewById(R.id.conctn_text);
        retry = (Button) getView().findViewById(R.id.button_retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
            }
        });

    }

}
