package com.invaderx.railway.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.invaderx.railway.R;
import com.invaderx.railway.adapters.TrainDetailAdapter;
import com.invaderx.railway.auth.LoginActivity;
import com.invaderx.railway.models.Trains;

import java.util.ArrayList;
import java.util.List;

public class TrainDetailsActivity extends AppCompatActivity {

    private TrainDetailAdapter mTrainDetailAdapter;
    private RecyclerView mRecyclerView;
    private List<Trains> trains = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    String addTrain = "add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_details);
        mRecyclerView = findViewById(R.id.recyclerview_traindetail);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mTrainDetailAdapter = new TrainDetailAdapter(trains,this);
        mRecyclerView.setAdapter(mTrainDetailAdapter);
        floatingActionButton = findViewById(R.id.fab);

        //database references
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        floatingActionButton.setOnClickListener(view ->
        {
            Intent i = new Intent(TrainDetailsActivity.this, TrainAddActivity.class);
            i.putExtra("add", addTrain);
            startActivity(i);
        });

        data();


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

    public void data(){
        databaseReference.child("Trains")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        trains.clear();
                        Log.v("Data Fetch", String.valueOf(dataSnapshot.getValue(Trains.class)));
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot t : dataSnapshot.getChildren()) {
                                Trains trains1 = t.getValue(Trains.class);
                                trains.add(new Trains(trains1.getTime(), trains1.getClass1A(), trains1.getClass2A(), trains1.getClass3A(),
                                        trains1.getClassCC(), trains1.getClassSL(), trains1.getdFri(), trains1.getdMon(), trains1.getdSat(),
                                        trains1.getdSun(), trains1.getdThur(), trains1.getdTue(), trains1.getdWed(), trains1.getSeat1A(),
                                        trains1.getSeat2A(), trains1.getSeat3A(), trains1.getSeatCC(), trains1.getSeatSL(),
                                        trains1.getStations(), trains1.gettName(), trains1.gettNumber()));
                            }
                            if(trains.size()==0){

                            }

                        }
                        else {

                        }
                        mRecyclerView.setAdapter(mTrainDetailAdapter);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(TrainDetailsActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
