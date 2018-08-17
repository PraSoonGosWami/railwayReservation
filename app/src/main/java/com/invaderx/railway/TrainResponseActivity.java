package com.invaderx.railway;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.invaderx.railway.adapters.TrainsAdapter;
import com.invaderx.railway.pojoClasses.Trains;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TrainResponseActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    TrainsAdapter trainsAdapter;
    List<Trains> trains = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    LinearLayout noTrains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_response);
        recyclerView=findViewById(R.id.searchRecyclerView);
        trainsAdapter=new TrainsAdapter(trains,this,recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar=findViewById(R.id.progressbar);
       /* getSupportActionBar().setTitle("Train Search"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        noTrains=findViewById(R.id.noTrains);
        noTrains.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        //setting progressbar color
        progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN );
        data();


       /* trains.add(new Trains("6:40-14:55","2846","1995","1080","0","450",
                1,1,0,0,1,1,1,65,135,205,0,640,
                "bhagalpur,kahalgon,pirpaitin,sahibganj,pakur,bolpur,shantiniketan,howrah",
                "Bgp Hwh Exp","13608"));
      */
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
                                trains.add(new Trains(trains1.getTime(),trains1.getClass1A(),trains1.getClass2A(),trains1.getClass3A(),
                                        trains1.getClassCC(),trains1.getClassSL(),trains1.getdFri(),trains1.getdMon(),trains1.getdSat(),
                                        trains1.getdSun(),trains1.getdThur(),trains1.getdTue(),trains1.getdWed(),trains1.getSeat1A(),
                                        trains1.getSeat2A(),trains1.getSeat3A(),trains1.getSeatCC(),trains1.getSeatSL(),
                                        trains1.getStations(),trains1.gettName(),trains1.gettNumber()));
                            }
                        }
                        else {
                            noTrains.setVisibility(View.VISIBLE);
                        }
                            progressBar.setVisibility(View.INVISIBLE);
                        recyclerView.setAdapter(trainsAdapter);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(TrainResponseActivity.this, "No Network", Toast.LENGTH_SHORT).show();
                    }
                });
    }
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

}



   /* String dayAvailable=userList.get(i).getDays();
    //matching pattern
    Boolean found = Arrays.asList(dayAvailable.split(",")).contains(daySearch);
                           if(found)*/