package com.invaderx.railway;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.invaderx.railway.adapters.PassengersAdapter;
import com.invaderx.railway.pojoClasses.Passengers;
import com.invaderx.railway.pojoClasses.Trains;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener{

    TextView bTrainNameNumber,bSrcDest,bTime,bClass,bFare;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressBar loader;
    LinearLayout passengers,noPassengersMessage;
    Button addPassengers,makePayment;
    ListView passengersList;
    List<Passengers> pArrayList = new ArrayList<>();

    String selectedGender;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Intent intent=getIntent();
        //getting Train number and class selected
        String trainNumber=intent.getStringExtra("trainNumber");
        int trainClass=intent.getIntExtra("trainClass",0);

        bTrainNameNumber=findViewById(R.id.bTrainNameNumber);
        bSrcDest=findViewById(R.id.bSrcDest);
        bTime=findViewById(R.id.bTime);
        bClass=findViewById(R.id.bClass);
        bFare=findViewById(R.id.bFare);
        loader=findViewById(R.id.loader);
        passengers=findViewById(R.id.passenegrs);
        passengersList=findViewById(R.id.passengersList);
        addPassengers=findViewById(R.id.addPassengers);
        addPassengers.setOnClickListener(this);
        makePayment=findViewById(R.id.makePayment);
        makePayment.setOnClickListener(this);
        noPassengersMessage=findViewById(R.id.noPassengerMessage);


        //firebase Database references
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        //if no passengers exist
        if(pArrayList.size()==0)
            noPassengersMessage.setVisibility(View.VISIBLE);
        else
            noPassengersMessage.setVisibility(View.INVISIBLE);

        //opens update dialog for the selected row
        passengersList.setOnItemClickListener((adapterView, view, i, l) ->
                customUpdateDialog(i));

        //setting adapter
        passengersList.setAdapter(new PassengersAdapter(this,R.layout.passeneger_model,pArrayList));

        //setting progress bar color
        loader.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN );


        //invisible until data loading is finished
        passengers.setVisibility(View.INVISIBLE);
        loader.setVisibility(View.VISIBLE);
        getCurrentTraindata(trainNumber,trainClass);


    }
    //gets selected train data for booking
    public void getCurrentTraindata(String trainNumber,int i){
        databaseReference.child("Trains").orderByChild("tNumber").equalTo(trainNumber)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Trains trains1=null;
                        Log.v("Data Fetch", String.valueOf(dataSnapshot.getValue(Trains.class)));
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot t : dataSnapshot.getChildren()) {
                                trains1 = t.getValue(Trains.class);
                            }
                            passengers.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.INVISIBLE);
                        }
                        else {
                           //handle no such trains
                        }
                        bTrainNameNumber.setText(trains1.gettName()+"  ("+trains1.gettNumber()+")");
                        //TODO:set src dest from TrainSearchActivity
                        bTime.setText("Time: "+trains1.getTime());
                        switch (i){
                            case 1:
                                bClass.setText("Class: AC 1A");
                                bFare.setText("Fare: "+trains1.getClass1A());
                                break;
                            case 2:
                                bClass.setText("Class: AC 2A");
                                bFare.setText("Fare: "+trains1.getClass2A());
                                break;
                            case 3:
                                bClass.setText("Class: AC 3A");
                                bFare.setText("Fare: "+trains1.getClass3A());
                                break;
                            case 4:
                                bClass.setText("Class: Sleeper");
                                bFare.setText("Fare: "+trains1.getClassSL());
                                break;
                            case 5:
                                bClass.setText("Class: CC");
                                bFare.setText("Fare: "+trains1.getClassCC());
                                break;
                                default:
                                    bClass.setText("Class: N/A");
                                    bFare.setText("Fare: N/A");
                                    break;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(BookingActivity.this, "Something went wrong\nTry again in a moment", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View view) {
      switch (view.getId()){
          case R.id.addPassengers:
              cusotmEntryDialog();
              break;
          case R.id.makePayment:
              Toast.makeText(this, ""+pArrayList.size(), Toast.LENGTH_SHORT).show();

      }
    }

    //custom popup dialog to enter passengers
    public void cusotmEntryDialog(){
        LayoutInflater layoutInflater =LayoutInflater.from(this);
        View view =layoutInflater.inflate(R.layout.passenger_add_model,null);
        final EditText passName,passAge;
        Spinner genderSpinner;
        passName=view.findViewById(R.id.pNameEditText);
        passAge=view.findViewById(R.id.pAgeEditText);
        genderSpinner=view.findViewById(R.id.genderSpinner);

        //gender drop down list
        ArrayList<String> gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        gender.add("Others");

        //spinner adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,gender);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(arrayAdapter);


        //getting the selected option from spinner
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0:
                        selectedGender="M";
                        break;
                    case 1:
                        selectedGender="F";
                        break;
                    case 2:
                        selectedGender="Ot";
                        break;
                        default:
                            selectedGender="M";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                    selectedGender="M";
            }
        });

        //custom dialog box
        LovelyCustomDialog lovelyCustomDialog =new LovelyCustomDialog(this);
        lovelyCustomDialog.setView(view)
        .setTopColorRes(R.color.colorPrimaryDark)
        .setTitle("Add Passengers")
        .setIcon(R.drawable.multiple_users32)
        .setListener(R.id.cancel, v -> {
            lovelyCustomDialog.dismiss();
        })
        .setListener(R.id.addP, v -> {

            if(TextUtils.isEmpty(passName.getText().toString())|| passName.getText().toString().equals(""))
                passName.setError("Please enter a name");
            else if(TextUtils.isEmpty(passAge.getText().toString())|| passAge.getText().toString().equals(""))
                passAge.setError("Please enter age");

            else {
                pArrayList.add(new Passengers(passName.getText().toString(),
                        Integer.parseInt(passAge.getText().toString()),
                        selectedGender));
                noPassengersMessage.setVisibility(View.INVISIBLE);
                lovelyCustomDialog.dismiss();
            }
        })
        .show();
    }

    public void customUpdateDialog(int positon){
        LayoutInflater layoutInflater =LayoutInflater.from(this);
        View view =layoutInflater.inflate(R.layout.passenger_add_model,null);
        final EditText passName,passAge;
        Spinner genderSpinner;
        passName=view.findViewById(R.id.pNameEditText);
        passAge=view.findViewById(R.id.pAgeEditText);
        genderSpinner=view.findViewById(R.id.genderSpinner);


        switch (pArrayList.get(positon).getpSex()){
            case "M":
                i=0;break;
            case "F":
                i=1;break;
            case "Ot":
                i=2;break;
                default:
                    i=0;break;
        }

        //gender drop down list
        ArrayList<String> gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        gender.add("Others");

        //spinner adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,gender);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(arrayAdapter);


        //getting the selected option from spinner
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0:
                        selectedGender="M";
                        break;
                    case 1:
                        selectedGender="F";
                        break;
                    case 2:
                        selectedGender="Ot";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedGender="M";
            }
        });

        passAge.setText(String.valueOf(pArrayList.get(positon).getpAge()));
        passName.setText(pArrayList.get(positon).getpName());
        genderSpinner.setSelection(i);




        //getting the selected option from spinner
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0:
                        selectedGender="M";
                        break;
                    case 1:
                        selectedGender="F";
                        break;
                    case 2:
                        selectedGender="Ot";
                        break;
                    default:
                        selectedGender="M";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedGender="M";
            }
        });

        //custom dialog box
        LovelyCustomDialog lovelyCustomDialog =new LovelyCustomDialog(this);
        lovelyCustomDialog.setView(view)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Update Passengers")
                .setIcon(R.drawable.multiple_users32)
                .setListener(R.id.cancel, v -> {
                    lovelyCustomDialog.dismiss();
                })
                .setListener(R.id.addP, v -> {

                    if(TextUtils.isEmpty(passName.getText().toString())|| passName.getText().toString().equals(""))
                        passName.setError("Please enter a name");
                    else if(TextUtils.isEmpty(passAge.getText().toString())|| passAge.getText().toString().equals(""))
                        passAge.setError("Please enter age");

                    else {
                        pArrayList.set(positon,(new Passengers(passName.getText().toString(),
                                Integer.parseInt(passAge.getText().toString()),
                                selectedGender)));
                        passengersList.setAdapter(new PassengersAdapter(this,R.layout.passeneger_model,pArrayList));
                        noPassengersMessage.setVisibility(View.INVISIBLE);
                        lovelyCustomDialog.dismiss();
                    }
                })
                .show();

    }
}
