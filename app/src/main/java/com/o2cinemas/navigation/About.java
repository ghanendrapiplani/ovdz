package com.o2cinemas.navigation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.o2cinemas.o2vidz.R;

public class About extends AppCompatActivity {
    TextView version, url, copygirht, right;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        version= (TextView) findViewById(R.id.about_version);
        url= (TextView) findViewById(R.id.url);
        copygirht= (TextView) findViewById(R.id.year);
        right = (TextView) findViewById(R.id.right);
    }
}
