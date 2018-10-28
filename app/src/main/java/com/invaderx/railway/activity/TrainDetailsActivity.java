package com.invaderx.railway.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.invaderx.railway.R;
import com.invaderx.railway.adapters.TrainDetailAdapter;
import com.invaderx.railway.auth.LoginActivity;

public class TrainDetailsActivity extends AppCompatActivity {

    TrainDetailAdapter mTrainDetailAdapter;
    private RecyclerView mRecyclerView;
    FloatingActionButton floatingActionButton;
    String addTrain = "add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_details);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_traindetail);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mTrainDetailAdapter = new TrainDetailAdapter();
        mRecyclerView.setAdapter(mTrainDetailAdapter);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(view ->
        {
            Intent i = new Intent(TrainDetailsActivity.this, TrainAddActivity.class);
            i.putExtra("add", addTrain);
            startActivity(i);
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.admin_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(TrainDetailsActivity.this, LoginActivity.class));
        }

        return true;
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }*/
}
