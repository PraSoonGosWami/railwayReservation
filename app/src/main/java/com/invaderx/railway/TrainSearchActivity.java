package com.invaderx.railway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class TrainSearchActivity extends AppCompatActivity {
    Button searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_search);
        searchButton=findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v->
        startActivity(new Intent(this,TrainResponseActivity.class)));

    }
}
