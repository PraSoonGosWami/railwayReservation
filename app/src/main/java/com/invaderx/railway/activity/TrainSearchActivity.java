package com.invaderx.railway.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.invaderx.railway.auth.LoginActivity;
import com.invaderx.railway.models.Stations;
import com.rupins.drawercardbehaviour.CardDrawerLayout;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class TrainSearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton searchButton;
    Toolbar toolbar;
    private CardDrawerLayout drawer;
    public static String source;
    public static String destination;
    public static Activity searchActivity;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String>station =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_search);
        EditText lsrc,ldest;
        TextInputLayout srcLayout,destLayout;
        searchActivity=this;
        lsrc=findViewById(R.id.lsrc);
        ldest=findViewById(R.id.ldesc);
        srcLayout=findViewById(R.id.srcTextLayout);
        destLayout=findViewById(R.id.destTextLayout);
        searchButton=findViewById(R.id.searchButton);

        //firebase Database references
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        //onClick for search Button
        searchButton.setOnClickListener(v->{
            if(TextUtils.isEmpty(lsrc.getText().toString()) || lsrc.getText().toString().equals(""))
                srcLayout.setError("Please Enter a source station");

            else if(TextUtils.isEmpty(ldest.getText().toString()) || ldest.getText().toString().equals(""))
                destLayout.setError("Please Enter a destination station");
            else if(lsrc.getText().toString().equals(ldest.getText().toString()))
                destLayout.setError("Source and destination can't  be same");

            else {
                source = lsrc.getText().toString();
                destination = ldest.getText().toString();
                startActivity(new Intent(this, TrainResponseActivity.class));
            }
        });

        //side nav
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Search Trains");
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);
        getUsername();

        ArrayList listOfStation = getStationList();
        //setting search views
        lsrc.setOnClickListener(v->{
            new SimpleSearchDialogCompat(this, "Search...",
                    "Search for Source Staion", null, stationList(listOfStation),
                    new SearchResultListener<Stations>() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat dialog, Stations item, int position) {
                           lsrc.setText(item.getTitle().toString());
                            dialog.dismiss();
                        }
                    }).show();
        });
        ldest.setOnClickListener(v->{
            new SimpleSearchDialogCompat(this, "Search...",
                    "Search for Destination Staion", null, stationList(listOfStation),
                    new SearchResultListener<Stations>() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat dialog, Stations item, int position) {
                            ldest.setText(item.getTitle().toString());
                            dialog.dismiss();
                        }
                    }).show();
        });


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.nav_logout:
                logoutDialog();
                break;
            case R.id.nav_bookings:
                startActivity(new Intent(this,MyBookings.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }
    private ArrayList<String> getStationList(){

        ProgressDialog dialog = ProgressDialog.show(this, "Sit back and relax",
                "Loading. Please wait...", true);
        databaseReference.child("Stations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    ArrayList<String> stn = (ArrayList<String>) dataSnapshot.getValue();
                    station.clear();
                    for (int i = 0; i<stn.size();i++){
                        station.add(stn.get(i));

                    }
                }
                else{
                    Toast.makeText(TrainSearchActivity.this, "Please try in a bit", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.v("station",String.valueOf(station.size()));
        return station;
    }
    private ArrayList<Stations> stationList(ArrayList<String> stn){
        ArrayList<Stations> list = new ArrayList<>();

        for (int i =0; i < stn.size();i++)
            list.add(new Stations(stn.get(i)));

        return list;
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        TrainSearchActivity.searchActivity.finish();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void logoutDialog(){
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(android.R.color.holo_red_dark)
                .setButtonsColorRes(R.color.color_button_default)
                .setIcon(R.drawable.logout)
                .setTitle("Logout")
                .setMessage("Are you sure you wan't to logout?")
                .setPositiveButton(android.R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       logout();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public void getUsername(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.nav_header_main,null);
        TextView username = view.findViewById(R.id.username);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            Log.v("Username",name);

            username.setText("User Name: " + name);
        } else {
            username.setText("No user name");
        }
    }
}
