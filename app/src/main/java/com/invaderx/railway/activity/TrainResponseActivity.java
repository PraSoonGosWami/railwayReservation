package com.invaderx.railway.activity;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.invaderx.railway.R;
import com.invaderx.railway.adapters.TrainsAdapter;
import com.invaderx.railway.models.Trains;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TrainResponseActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private TrainsAdapter trainsAdapter;
    private List<Trains> trains = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private LottieAnimationView lottieAnimationView;
    private LinearLayout noTrains;
    public static Activity trainResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_response);
        //getting reference of this activity for finishing it after successful payment
        trainResponse=this;

        setTitle(TrainSearchActivity.source.toUpperCase()+" → "+TrainSearchActivity.destination.toUpperCase());
        getSupportActionBar().setSubtitle(getDate());
        getSupportActionBar().setElevation(0);

        //binding views
        recyclerView=findViewById(R.id.searchRecyclerView);
        trainsAdapter=new TrainsAdapter(trains,this,recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lottieAnimationView=findViewById(R.id.animation_view);
        //database references
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        //layout for no trains in the route
        noTrains=findViewById(R.id.noTrains);
        noTrains.setVisibility(View.INVISIBLE);

        lottieAnimationView.playAnimation();
        //setting progressbar color

        data();
        recyclerView.setAdapter(trainsAdapter);
    }
    //get search data
    public void data(){
        String day = getToday();
        databaseReference.child("Trains").orderByChild(day).equalTo(1)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        trains.clear();
                        Log.v("Data Fetch", String.valueOf(dataSnapshot.getValue(Trains.class)));
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot t : dataSnapshot.getChildren()) {
                                Trains trains1 = t.getValue(Trains.class);
                                ArrayList<String> availableStation=trains1.getStations();
                                String src=TrainSearchActivity.source.toLowerCase();
                                String dest=TrainSearchActivity.destination.toLowerCase();
                                Boolean srcFound = availableStation.contains(src);
                                Boolean destFound = availableStation.contains(dest);
                                int srcPos= availableStation.indexOf(src);
                                int destPos= availableStation.indexOf(dest);
                                if(srcFound && destFound && (srcPos<destPos)) {
                                    trains.add(new Trains(trains1.getTime(), trains1.getClass1A(), trains1.getClass2A(), trains1.getClass3A(),
                                            trains1.getClassCC(), trains1.getClassSL(), trains1.getdFri(), trains1.getdMon(), trains1.getdSat(),
                                            trains1.getdSun(), trains1.getdThur(), trains1.getdTue(), trains1.getdWed(), trains1.getSeat1A(),
                                            trains1.getSeat2A(), trains1.getSeat3A(), trains1.getSeatCC(), trains1.getSeatSL(),
                                            trains1.getStations(), trains1.gettName(), trains1.gettNumber()));
                                }
                            }
                            if(trains.size()==0)
                                noTrains.setVisibility(View.VISIBLE);
                        }
                        else {
                            noTrains.setVisibility(View.VISIBLE);
                        }
                        recyclerView.setAdapter(trainsAdapter);
                        lottieAnimationView.setVisibility(View.INVISIBLE);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(TrainResponseActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //checks if there is internet connection
    public void noInternet(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (!connected) {
                   // progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(TrainResponseActivity.this, "Not connected", Toast.LENGTH_SHORT).show();
                }
                else {
                   // progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    //getting current day for seaching trains on today's date
    public String getToday(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                return "dSun";
            case Calendar.MONDAY:
                return "dMon";
            case Calendar.TUESDAY:
                return "dTue";
            case Calendar.WEDNESDAY:
                return "dWed";
            case Calendar.THURSDAY:
                return "dThur";
            case Calendar.FRIDAY:
                return "dFri";
            case Calendar.SATURDAY:
                return "dSat";
        }
        return "null";
    }

    //get current date
    public String getDate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }


}
