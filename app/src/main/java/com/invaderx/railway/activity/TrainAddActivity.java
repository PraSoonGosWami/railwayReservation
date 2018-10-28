package com.invaderx.railway.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.invaderx.railway.R;


public class TrainAddActivity extends AppCompatActivity {
    CheckBox ma1;
    CheckBox ma2;
    CheckBox ma3;
    CheckBox msl;
    CheckBox mcc;

    LinearLayout mLa1;
    LinearLayout mLa2;
    LinearLayout mLa3;
    LinearLayout mLsl;
    LinearLayout mLcc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_add);
        ma1 = (CheckBox) findViewById(R.id.a1);
        ma2 = (CheckBox) findViewById(R.id.a2);
        ma3 = (CheckBox) findViewById(R.id.a3);
        msl = (CheckBox) findViewById(R.id.sl);
        mcc = (CheckBox) findViewById(R.id.cc);

        mLa1 = (LinearLayout) findViewById(R.id.a1linearlayout);
        mLa2 = (LinearLayout) findViewById(R.id.a2linearlayout);
        mLa3 = (LinearLayout) findViewById(R.id.a3linearlayout);
        mLsl = (LinearLayout) findViewById(R.id.sllinearlayout);
        mLcc = (LinearLayout) findViewById(R.id.cclinearlayout);

        ma1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ma1.isChecked()) {
                    mLa1.setVisibility(View.VISIBLE);
                } else {
                    mLa1.setVisibility(View.GONE);
                }
            }
        });
        ma2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ma2.isChecked()) {
                    mLa2.setVisibility(View.VISIBLE);
                } else {
                    mLa2.setVisibility(View.GONE);
                }
            }
        });
        ma3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ma3.isChecked()) {
                    mLa3.setVisibility(View.VISIBLE);
                } else {
                    mLa3.setVisibility(View.GONE);
                }
            }
        });
        msl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (msl.isChecked()) {
                    mLsl.setVisibility(View.VISIBLE);
                } else {
                    mLsl.setVisibility(View.GONE);
                }
            }
        });
        mcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mcc.isChecked()) {
                    mLcc.setVisibility(View.VISIBLE);
                } else {
                    mLcc.setVisibility(View.GONE);
                }
            }
        });


    }
}
