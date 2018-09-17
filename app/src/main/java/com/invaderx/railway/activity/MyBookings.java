package com.invaderx.railway.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.invaderx.railway.R;
import com.invaderx.railway.adapters.TicketAdapter;
import com.invaderx.railway.models.Ticket;

import java.util.ArrayList;
import java.util.List;


public class MyBookings extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        bookingsRecyclerView = findViewById(R.id.bookingsRecyclerView);
        noBookingMessage=findViewById(R.id.noBookingMessage);
        progressBar=findViewById(R.id.ticketProgressBar);

        getSupportActionBar().setElevation(0);
        setTitle("My Bookings");
        //database references
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        //ticket adapter
        ticketAdapter = new TicketAdapter(ticketList,this);
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
                                ,ticket1.getPeople(),ticket1.getSeatNo(),ticket1.getPnr()));
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
                        Toast.makeText(MyBookings.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
