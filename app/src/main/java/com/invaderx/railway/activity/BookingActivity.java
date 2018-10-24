package com.invaderx.railway.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.invaderx.railway.R;
import com.invaderx.railway.adapters.PassengersAdapter;
import com.invaderx.railway.models.Passengers;
import com.invaderx.railway.models.Ticket;
import com.invaderx.railway.models.Trains;
import com.invaderx.railway.models.UserProfile;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static maes.tech.intentanim.CustomIntent.customType;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView bTrainNameNumber,bSrcDest,bTime,bClass,bFare;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar loader;
    private LinearLayout passengers,noPassengersMessage;
    private Button addPassengers,makePayment;
    private ListView passengersList;
    private ArrayList<Passengers> pArrayList = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String selectedGender,userUID,trainNumber;
    private int i , availableSeats ,trainClass;
    private String sFare,travelClass,updateFareofClass;
    private Ticket ticket;
    private PopupWindow popWindow;
    private String baseclass = "";
    private int getAmount=0;
    private int totalFare=0, wallet=0,flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Intent intent=getIntent();
        customType(BookingActivity.this,"fadein-to-fadeout");

        getSupportActionBar().setElevation(0);
        //getting Train number and class selected
        trainNumber=intent.getStringExtra("trainNumber");
        trainClass=intent.getIntExtra("trainClass",0);

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
        addPassengers.setEnabled(true);
        makePayment=findViewById(R.id.makePayment);
        makePayment.setOnClickListener(this);
        noPassengersMessage=findViewById(R.id.noPassengerMessage);

        //firebase Database references
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        //firebase auth reference
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        userUID=firebaseUser.getUid();


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

        getWalletAmount();


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addPassengers:
                cusotmEntryDialog();
                addPassengers.setEnabled(false);
                break;
            case R.id.makePayment:
                if (pArrayList.size()>0)
                    paymentGateway();
                else
                    Snackbar.make(findViewById(android.R.id.content),"No Passengers added",Snackbar.LENGTH_SHORT).show();

                break;

        }
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
                        bSrcDest.setText(TrainSearchActivity.source.toUpperCase()+" → "+TrainSearchActivity.destination.toUpperCase());
                        bTime.setText("Time: "+trains1.getTime());
                        switch (i){
                            case 1:
                                bClass.setText("Class: AC 1A");
                                baseclass="seat1A";
                                bFare.setText("Fare: "+trains1.getClass1A());
                                //to get avalable seats to generate seat no and reduce the no of seats after booking
                                availableSeats=trains1.getSeat1A();
                                //to get fare of selected class to caluclate the total amount to be paid during payment
                                sFare=trains1.getClass1A();
                                break;
                            case 2:
                                bClass.setText("Class: AC 2A");
                                baseclass="seat2A";
                                bFare.setText("Fare: "+trains1.getClass2A());
                                //to get avalable seats to generate seat no and reduce the no of seats after
                                availableSeats=trains1.getSeat2A();
                                //to get fare of selected class to caluclate the total amount to be paid during payment
                                sFare=trains1.getClass2A();
                                break;
                            case 3:
                                bClass.setText("Class: AC 3A");
                                baseclass="seat3A";
                                bFare.setText("Fare: "+trains1.getClass3A());
                                //to get avalable seats to generate seat no and reduce the no of seats after
                                availableSeats=trains1.getSeat3A();
                                sFare=trains1.getClass3A();
                                break;
                            case 4:
                                bClass.setText("Class: Sleeper");
                                baseclass="seatSL";
                                bFare.setText("Fare: "+trains1.getClassSL());
                                //to get avalable seats to generate seat no and reduce the no of seats after
                                availableSeats=trains1.getSeatSL();
                                //to get fare of selected class to caluclate the total amount to be paid during payment
                                sFare=trains1.getClassSL();
                                break;
                            case 5:
                                bClass.setText("Class: CC");
                                baseclass="seatCC";
                                bFare.setText("Fare: "+trains1.getClassCC());
                                //to get avalable seats to generate seat no and reduce the no of seats after
                                availableSeats=trains1.getSeatCC();
                                //to get fare of selected class to caluclate the total amount to be paid during payment
                                sFare=trains1.getClassCC();
                                break;
                                default:
                                    bClass.setText("Class: N/A");
                                    baseclass="";
                                    bFare.setText("Fare: N/A");
                                    //to get avalable seats to generate seat no and reduce the no of seats after
                                    availableSeats=0;
                                    //to get fare of selected class to caluclate the total amount to be paid during payment
                                    sFare="0";
                                    break;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(BookingActivity.this, "Something went wrong\nTry again in a moment", Toast.LENGTH_SHORT).show();
                    }
                });
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
            addPassengers.setEnabled(true);
        })
        .setListener(R.id.addP, v -> {
            addPassengers.setEnabled(true);

            if(TextUtils.isEmpty(passName.getText().toString())|| passName.getText().toString().equals(""))
                passName.setError("Please enter a name");
            else if(TextUtils.isEmpty(passAge.getText().toString())|| passAge.getText().toString().equals(""))
                passAge.setError("Please enter age");

            else if(Integer.parseInt(passAge.getText().toString())<5)
                passAge.setError("Age must be greater than 5");


            else {
                pArrayList.add(new Passengers(passName.getText().toString(),
                        Integer.parseInt(passAge.getText().toString()),
                        selectedGender));
                noPassengersMessage.setVisibility(View.INVISIBLE);
                lovelyCustomDialog.dismiss();
            }
        })
        .show();
        addPassengers.setEnabled(true);
    }

    //custom popup dialog to update passengers
    public void customUpdateDialog(int positon){
        LayoutInflater layoutInflater =LayoutInflater.from(this);
        View view =layoutInflater.inflate(R.layout.passenger_add_model,null);
        final EditText passName,passAge;
        final Button addp;
        Spinner genderSpinner;
        passName=view.findViewById(R.id.pNameEditText);
        passAge=view.findViewById(R.id.pAgeEditText);
        genderSpinner=view.findViewById(R.id.genderSpinner);
        addp=view.findViewById(R.id.addP);
        addp.setText("Update");


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

                    else if(Integer.parseInt(passAge.getText().toString())<5)
                        passAge.setError("Age must be greater than 5");

                    else {
                        pArrayList.set(positon,(new Passengers(passName.getText().toString(),
                                Integer.parseInt(passAge.getText().toString()),
                                selectedGender)));
                        passengersList.setAdapter(new PassengersAdapter(this,R.layout.passeneger_model,pArrayList));
                        Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show();
                        noPassengersMessage.setVisibility(View.INVISIBLE);
                        lovelyCustomDialog.dismiss();
                    }
                })
                .show();

    }

    //confirms booking and generates ticket in database
    public void Payment(){
        String seatNo ="";
        switch (trainClass){
            case 1:
                travelClass="A1 ";
                updateFareofClass="seat1A";
                break;
            case 2:
                travelClass="A2 ";
                updateFareofClass="seat2A";
                break;
            case 3:
                travelClass="B1 ";
                updateFareofClass="seat3A";
                break;
            case 4:
                travelClass="SL ";
                updateFareofClass="seatSL";
                break;
            case 5:
                travelClass="CC ";
                updateFareofClass="seatCC";
                break;
            default:
                travelClass="N/A ";
                updateFareofClass="N/A";
                break;
        }
        if(availableSeats<pArrayList.size()){
            for(int i=1;i<=pArrayList.size();i++){
                seatNo+="WL "+i+"\n";
            }
        }
        else{
            for(int i=1;i<=pArrayList.size();i++){
                seatNo+=travelClass+String.valueOf(availableSeats)+"\n";
                availableSeats=availableSeats-1;
            }
        }

        //calculation of total fare
        totalFare=(Integer.parseInt(sFare))*(pArrayList.size());

        String pnr = generatePNR();
        ticket=new Ticket(bTrainNameNumber.getText().toString(),TrainSearchActivity.source,TrainSearchActivity.destination,
                bClass.getText().toString(),bTime.getText().toString(),String.valueOf(totalFare)
                ,TrainSearchActivity.date,pArrayList,seatNo,pnr,trainNumber,baseclass);
        databaseReference.child("Ticket").child(userUID).child(pnr).setValue(ticket)
        .addOnSuccessListener(aVoid ->{
                updateSeatAvalabilty(availableSeats,trainNumber,updateFareofClass);
        });

    }

    //generates pnr Number
    public String generatePNR(){
        long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(number);
    }

    //updates the seat avalabilty after payment of the ticket
    public void updateSeatAvalabilty(int newAvailbaeSeat,String trainNumber,String classSeat){
            databaseReference.child("Trains").child(trainNumber).child(classSeat).setValue(newAvailbaeSeat)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(BookingActivity.this, "Ticket generated Successfully", Toast.LENGTH_SHORT).show();
                    });
    }

    //gets wallet amount
    public void getWalletAmount(){
        databaseReference.child("UserProfile").orderByChild("uid").equalTo(userUID)
                .addValueEventListener(new ValueEventListener() {
                    UserProfile userProfile = null;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot data : dataSnapshot.getChildren()){
                                userProfile=data.getValue(UserProfile.class);
                            }
                            getAmount=userProfile.getWallet();
                        }
                        Log.e("Wallet",""+getAmount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //opens payment gateway
    public void paymentGateway(){

        ImageView cancelPayment;
        RelativeLayout hidable,showable;
        LinearLayout walletClick,upiClick,cardClick,paytmClick,allDoneLayout;
        TextView paymentWallet,upiPayment,debitCardPayment,paytmPayment,total,selectedPayment,date;
        Button doPayment,allDone;


        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflatedView = layoutInflater.inflate(R.layout.payment_popup, null,false);

        cancelPayment=inflatedView.findViewById(R.id.cancelPayment);
        hidable=inflatedView.findViewById(R.id.hidable);
        showable=inflatedView.findViewById(R.id.showable);
        walletClick=inflatedView.findViewById(R.id.walletClick);
        upiClick=inflatedView.findViewById(R.id.upiClick);
        cardClick=inflatedView.findViewById(R.id.cardClick);
        paytmClick=inflatedView.findViewById(R.id.paytmClick);
        allDoneLayout=inflatedView.findViewById(R.id.allDoneLayout);
        paymentWallet=inflatedView.findViewById(R.id.paymentWallet);
        upiPayment=inflatedView.findViewById(R.id.upiPayment);
        debitCardPayment=inflatedView.findViewById(R.id.debitCardPayment);
        paytmPayment=inflatedView.findViewById(R.id.paytmPayment);
        total=inflatedView.findViewById(R.id.totalFare);
        selectedPayment=inflatedView.findViewById(R.id.selectedPayment);
        date=inflatedView.findViewById(R.id.date);
        doPayment=inflatedView.findViewById(R.id.doPayment);
        allDone=inflatedView.findViewById(R.id.allDone);

        // get device size
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        //mDeviceHeight = size.y;
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // set height depends on the device size
        popWindow = new PopupWindow(inflatedView, width,height-60, true );
        popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popWindow.setAnimationStyle(R.style.PopupAnimation);

        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(inflatedView, Gravity.BOTTOM, 0,100);

        total.setText("Total Fare: "+(Integer.parseInt(sFare))*(pArrayList.size()));
        date.setText("Date: "+TrainSearchActivity.date);
        cancelPayment.setOnClickListener(v->{
            popWindow.dismiss();
            hidable.setVisibility(View.VISIBLE);
            showable.setVisibility(View.INVISIBLE);
            allDoneLayout.setVisibility(View.INVISIBLE);
        });

        //getting payment details--------------------------------------------------------------------------------------
        databaseReference.child("UserProfile").orderByChild("uid").equalTo(userUID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserProfile userProfile1 = null;
                        if(dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                userProfile1=dataSnapshot1.getValue(UserProfile.class);
                            }

                            if(userProfile1.getUpi().equals("0"))
                                upiPayment.setText("Link UPI");
                            else
                                upiPayment.setText(userProfile1.getUpi());
                            if(userProfile1.getCard().equals("0"))
                                debitCardPayment.setText("Link Debit/Credit Card");
                            else
                                debitCardPayment.setText(userProfile1.getCard());
                            if(userProfile1.getPaytm().equals("0"))
                                paytmPayment.setText("Link Paytm");
                            else
                                paytmPayment.setText(userProfile1.getPaytm());
                            paymentWallet.setText("₹"+userProfile1.getWallet());
                            wallet=userProfile1.getWallet();
                        }
                        else {
                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Snackbar.make(findViewById(android.R.id.content), ""+databaseError.getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                });
        //-----------------------------------------------------------------------------------------------------------------

        walletClick.setOnClickListener(v -> {
            if(wallet<(Integer.parseInt(sFare))*(pArrayList.size()))
                Toast.makeText(this, "Insufficient Amount\n Refill your wallet in profile section", Toast.LENGTH_SHORT).show();
            else{
                hidable.setVisibility(View.INVISIBLE);
                showable.setVisibility(View.VISIBLE);
                selectedPayment.setText("Railway wallet ("+paymentWallet.getText().toString()+")");
                flag = 1;
            }

        });
        upiClick.setOnClickListener(v -> {
            if(upiPayment.getText().toString().equals("Link UPI"))
                Toast.makeText(this, "UPI not Linked please link it in profile section", Toast.LENGTH_SHORT).show();
            else {
                hidable.setVisibility(View.INVISIBLE);
                showable.setVisibility(View.VISIBLE);
                selectedPayment.setText("UPI Id  ("+upiPayment.getText().toString()+")");
            }


        });
        cardClick.setOnClickListener(v -> {
            if(debitCardPayment.getText().toString().equals("Link Debit/Credit Card"))
                Toast.makeText(this, "Debitt/Credit Card not Linked please link it in profile section", Toast.LENGTH_SHORT).show();
            else {
                hidable.setVisibility(View.INVISIBLE);
                showable.setVisibility(View.VISIBLE);
                selectedPayment.setText("Debit/Credit Card  ("+debitCardPayment.getText().toString()+")");
            }

        });
        paytmClick.setOnClickListener(v -> {
            if(paytmPayment.getText().toString().equals("Link Paytm"))
                Toast.makeText(this, "Paytm not Linked please link it in profile section", Toast.LENGTH_SHORT).show();
            else {
                hidable.setVisibility(View.INVISIBLE);
                showable.setVisibility(View.VISIBLE);
                selectedPayment.setText("Paytm Number ("+paytmPayment.getText().toString()+")");
            }

        });

        doPayment.setOnClickListener(v -> {
            hidable.setVisibility(View.INVISIBLE);
            showable.setVisibility(View.INVISIBLE);
            allDoneLayout.setVisibility(View.VISIBLE);
            Payment();
            if(flag == 1){
                flag=0;
                updateWallet();
            }
        });

        allDone.setOnClickListener(v -> {
            hidable.setVisibility(View.VISIBLE);
            showable.setVisibility(View.INVISIBLE);
            allDoneLayout.setVisibility(View.INVISIBLE);
            finish();
            TrainResponseActivity.trainResponse.finish();
        });

    }

    //updates wallet amount after cancellation
    public void updateWallet(){
        databaseReference.child("UserProfile").child(userUID).child("wallet").setValue(wallet-(Integer.parseInt(sFare))*(pArrayList.size()));

    }


}
