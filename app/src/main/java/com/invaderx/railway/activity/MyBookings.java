package com.invaderx.railway.activity;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
import com.invaderx.railway.adapters.TicketAdapter;
import com.invaderx.railway.models.Passengers;
import com.invaderx.railway.models.Ticket;
import com.invaderx.railway.models.Trains;
import com.invaderx.railway.models.UserProfile;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static maes.tech.intentanim.CustomIntent.customType;


public class MyBookings extends AppCompatActivity implements TicketAdapter.ListItemClickListner {
    private RecyclerView bookingsRecyclerView;
    private List<Ticket> ticketList = new ArrayList<>();
    private TicketAdapter ticketAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private LinearLayout noBookingMessage;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userUID;
    private ArrayList<Passengers> passList;
    private PopupWindow popWindow;
    private String PNR,seatAlloted,freshSeats="",trainNumber="",currentclass="";
    private int fare = 0,i=0;
    private int getAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customType(MyBookings.this,"fadein-to-fadeout");
        setContentView(R.layout.activity_my_bookings);
        bookingsRecyclerView = findViewById(R.id.bookingsRecyclerView);
        noBookingMessage=findViewById(R.id.noBookingMessage);
        progressBar=findViewById(R.id.ticketProgressBar);
        passList = new ArrayList<>();
        getSupportActionBar().setElevation(0);
        setTitle("My Bookings");
        //database references
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        //ticket adapter
        ticketAdapter = new TicketAdapter(ticketList,this,this);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //firebase auth reference
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        userUID=firebaseUser.getUid();

        //progress bar visibilty visible
        progressBar.setVisibility(View.VISIBLE);
        noBookingMessage.setVisibility(View.INVISIBLE);


        getTicket();
        bookingsRecyclerView.setAdapter(ticketAdapter);


        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.passeneger_model,null);
        Button deletePassengers = view.findViewById(R.id.deletePassengers);
        deletePassengers.setVisibility(View.INVISIBLE);

        getWalletAmount();



    }
    //gets all the tickets of the current user
    public void getTicket(){

        databaseReference.child("Ticket").child(userUID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ticketList.clear();
                        Log.v("Ticket",String.valueOf(dataSnapshot.getValue(Ticket.class)));
                        if (dataSnapshot.exists()){
                            for(DataSnapshot tt : dataSnapshot.getChildren()){
                                Ticket ticket1 = tt.getValue(Ticket.class);
                                ticketList.add(new Ticket(ticket1.getTicketNameNumber(),ticket1.getSrc(),ticket1.getDest()
                                ,ticket1.getTravelClass(),ticket1.getTime(),ticket1.getFare(),ticket1.getDate()
                                ,ticket1.getPeople(),ticket1.getSeatNo(),ticket1.getPnr(),ticket1.getTrainNo(),ticket1.getBaseClass()));
                            }
                            if(ticketList.size()==0){
                                noBookingMessage.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            noBookingMessage.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        bookingsRecyclerView.setAdapter(ticketAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Snackbar.make(findViewById(android.R.id.content),""+databaseError.getMessage(),Snackbar.LENGTH_SHORT).show();

                    }
                });
    }

    //get current ticket for specific pnr
    public void getCurrentTicket(String pnr){
        TextView exPNR,exSeatNo,exClass,exTrainNameNum,exSrcDest,exDate,exTime,exFare;
        ListView exPassengerList;
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ProgressBar progressBar;
        CardView eXTicket;
        FloatingActionButton cancelFAB;
        // inflate the custom popup layout
        View inflatedView = layoutInflater.inflate(R.layout.ticket_popup, null,false);


        exPNR = inflatedView.findViewById(R.id.exPNR);
        exSeatNo = inflatedView.findViewById(R.id.exSeatNo);
        exClass = inflatedView.findViewById(R.id.exClass);
        exTrainNameNum = inflatedView.findViewById(R.id.exTrainNameNum);
        exSrcDest = inflatedView.findViewById(R.id.exSrcDest);
        exDate = inflatedView.findViewById(R.id.exDate);
        exTime = inflatedView.findViewById(R.id.exTime);
        exFare = inflatedView.findViewById(R.id.exFare);
        progressBar = inflatedView.findViewById(R.id.exProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        eXTicket = inflatedView.findViewById(R.id.exTicket);
        eXTicket.setVisibility(View.INVISIBLE);
        exPassengerList = inflatedView.findViewById(R.id.exPassengerList);
        cancelFAB = inflatedView.findViewById(R.id.cancelFAB);


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

        databaseReference.child("Ticket").child(userUID).orderByChild("pnr").equalTo(pnr)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Ticket ticket2=null;
                        if (dataSnapshot.exists()){
                            passList.clear();
                            for(DataSnapshot t : dataSnapshot.getChildren()) {
                                ticket2 = t.getValue(Ticket.class);
                            }
                            eXTicket.setVisibility(View.VISIBLE);
                            passList.addAll(ticket2.getPeople());
                            exPassengerList.setAdapter(new PassengersAdapter(MyBookings.this,R.layout.passeneger_model,passList));
                            exPNR.setText(ticket2.getPnr());
                            exSeatNo.setText(ticket2.getSeatNo());
                            exClass.setText(ticket2.getTravelClass());
                            exTrainNameNum.setText(ticket2.getTicketNameNumber());
                            exSrcDest.setText("From: "+ticket2.getSrc()+"\n"+"To: "+ticket2.getDest());
                            exDate.setText(ticket2.getDate());
                            exTime.setText(ticket2.getTime());
                            exFare.setText("Fare: ₹"+ticket2.getFare());
                            fare=Integer.parseInt(ticket2.getFare());
                            seatAlloted=ticket2.getSeatNo();
                            trainNumber = ticket2.getTrainNo();
                            currentclass = ticket2.getBaseClass();
                        }

                        progressBar.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Snackbar.make(findViewById(android.R.id.content),""+databaseError.getMessage(),Snackbar.LENGTH_SHORT).show();


                    }
                });


        //gets current available seats
        getCurrentSeats();


        cancelFAB.setOnClickListener(v -> {
            if(compareDates(exDate.getText().toString())==false)
                cancelTicket(pnr);
            else
                Toast.makeText(this, "Cannot cancel back dated tickets", Toast.LENGTH_SHORT).show();
        });

        exPassengerList.setOnItemClickListener((parent, view, position, id) -> {
            if(compareDates(exDate.getText().toString())==false){
                if (passList.size() < 2)
                    cancelTicket(pnr);
                else {
                    cancelPerson(pnr, position);
                }
            }
            else
                Toast.makeText(this, "Cannot cancel back dated tickets", Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        PNR = ((TextView) bookingsRecyclerView.findViewHolderForAdapterPosition(clickedItemIndex)
                .itemView.findViewById(R.id.ticketPNR)).getText().toString();
        getCurrentTicket(PNR);
    }

    //cancel whole ticket
    public void cancelTicket(String pnr){
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(android.R.color.holo_red_dark)
                .setButtonsColorRes(android.R.color.holo_orange_dark)
                .setIcon(android.R.drawable.ic_menu_delete)
                .setTitle("Cancel Ticket")
                .setMessage("Are you sure you want to cancel this ticket?")
                .setPositiveButton("Yes", v -> {
                    databaseReference.child("Ticket").child(userUID).child(pnr).setValue(null)
                            .addOnSuccessListener(aVoid -> {

                                updateSeatAvalabilty(passList.size());
                                updateWallet(fare);
                                popWindow.dismiss();

                            });
                })
                .setNegativeButton("No",null)
                .show();
        getCurrentSeats();

    }

    //cancel only selected passengers
    public void cancelPerson(String PNR,int position){

        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(android.R.color.holo_red_dark)
                .setButtonsColorRes(android.R.color.holo_orange_dark)
                .setIcon(android.R.drawable.ic_menu_delete)
                .setTitle("Cancel Passenger")
                .setMessage("Are you sure you want to cancel this particular passenger?")
                .setPositiveButton("Yes", v -> {
                    passList.remove(position);
                    //removes selected passenger ticket
                    databaseReference.child("Ticket").child(userUID).child(PNR).child("people").setValue(passList)
                            .addOnSuccessListener(p->{
                                freshSeats="";
                                ArrayList<String> deletedSeats= new ArrayList<>(Arrays.asList(seatAlloted.split("\\r?\\n")));
                                int n = deletedSeats.size();
                                deletedSeats.remove(n-1);
                                for(int i=0; i<deletedSeats.size();i++){
                                    freshSeats+=deletedSeats.get(i)+"\n";
                                }
                                //updates seat no after cancellation
                                databaseReference.child("Ticket").child(userUID).child(PNR).child("seatNo").setValue(freshSeats);

                                //updates fare of ticket
                                String newFare = String.valueOf(fare-(fare/n));
                                updateWallet(fare/n);
                                databaseReference.child("Ticket").child(userUID).child(PNR).child("fare").setValue(newFare)
                                        .addOnSuccessListener(m->{
                                            updateSeatAvalabilty(1);
                                        });


                            });
                })
                .setNegativeButton("No",null)
                .show();
        getCurrentSeats();


    }

    //update trainSeats after cancellation
    public void updateSeatAvalabilty(int cancelledSeats){

        int s = i + cancelledSeats;
        Log.i("SEATS",String.valueOf(s)+"\t"+i+"\t"+cancelledSeats);
        databaseReference.child("Trains").child(trainNumber).child(currentclass).setValue(s);
    }

    //gets current available seats
    public void getCurrentSeats(){

        Log.v("Train no",trainNumber);
        databaseReference.child("Trains").orderByChild("tNumber").equalTo(trainNumber)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Trains trains1=null;
                        Log.v("Data Ticket", String.valueOf(dataSnapshot.getValue(Trains.class)));
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot t : dataSnapshot.getChildren()) {
                                trains1 = t.getValue(Trains.class);
                            }

                            switch (currentclass) {
                                case "seat1A":
                                    i = trains1.getSeat1A();
                                    break;
                                case "seat2A":
                                    i = trains1.getSeat2A();
                                    break;
                                case "seat3A":
                                    i = trains1.getSeat3A();
                                    break;
                                case "seatSL":
                                    i = trains1.getSeatSL();
                                    break;
                                case "seatCC":
                                    i = trains1.getSeatCC();
                                    break;

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    //updates wallet amount after cancellation
    public void updateWallet(int cancelledAmount){
        databaseReference.child("UserProfile").child(userUID).child("wallet").setValue(getAmount+cancelledAmount)
                .addOnSuccessListener(m->{
                    popWindow.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), "Cancel Successful\nMoney refunded to wallet", Snackbar.LENGTH_LONG).show();

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

    //compares previous date
    public boolean compareDates(String getDate){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(getDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (System.currentTimeMillis() > strDate.getTime())
            return true;
        else
            return false;

    }
}
