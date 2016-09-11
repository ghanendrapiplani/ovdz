package com.o2cinemas.o2vidz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.o2cinemas.utilities.SharedPreferenceUtil;

/**
 * Created by admin on 9/9/16.
 */
public class SettingActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    TextView tvTaskNum;
    int downloadTaskNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        Button btnEditPath = (Button)findViewById(R.id.btnEditPath);
        btnEditPath.setOnClickListener(this);

        Button btnIncrease = (Button) findViewById(R.id.btnIncrease);
        btnIncrease.setOnClickListener(this);

        Button btnDecrease = (Button) findViewById(R.id.btnDecrease);
        btnDecrease.setOnClickListener(this);

        tvTaskNum = (TextView) findViewById(R.id.tvTaskNum);

        Switch switchResume = (Switch) findViewById(R.id.switchResume);
        switchResume.setOnCheckedChangeListener(this);

        Switch switchWifi = (Switch) findViewById(R.id.switchWift);
        switchWifi.setOnCheckedChangeListener(this);

        setUpData();

    }

    private void setUpData(){
        downloadTaskNum = SharedPreferenceUtil.getDownloadTaskNum(this);

        tvTaskNum.setText(String.valueOf(downloadTaskNum));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnEditPath:

                break;
            case R.id.btnIncrease:
                if (downloadTaskNum < 4) {
                    downloadTaskNum ++;
                    SharedPreferenceUtil.setDownloadTaskNum(this, downloadTaskNum);
                    tvTaskNum.setText(String.valueOf(downloadTaskNum));
                }

                break;
            case R.id.btnDecrease:
                if (downloadTaskNum > 1) {
                    downloadTaskNum --;
                    SharedPreferenceUtil.setDownloadTaskNum(this, downloadTaskNum);
                    tvTaskNum.setText(String.valueOf(downloadTaskNum));
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchResume:

                break;
            case R.id.switchWift:

                break;
        }
    }
}
