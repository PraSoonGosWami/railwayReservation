package com.invaderx.railway.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.invaderx.railway.R;
import com.invaderx.railway.auth.LoginActivity;
import com.invaderx.railway.models.Stations;
import com.rupins.drawercardbehaviour.CardDrawerLayout;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

import static maes.tech.intentanim.CustomIntent.customType;

public class TrainSearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton searchButton;
    Toolbar toolbar;
    private CardDrawerLayout drawer;
    public static String source;
    public static String destination;
    NavigationView navigationView;
    public static Activity searchActivity;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String>station =new ArrayList<>();
    private int mYear, mMonth, mDay;
    private TextView dateSelecter;
    public static String day,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_search);
        customType(TrainSearchActivity.this,"fadein-to-fadeout");
        EditText lsrc,ldest;
        searchActivity=this;
        lsrc=findViewById(R.id.lsrc);
        ldest=findViewById(R.id.ldesc);
        searchButton=findViewById(R.id.searchButton);
        day=getToday();
        date=getDate();
        //firebase Database references
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        dateSelecter=findViewById(R.id.dateSelect);
        dateSelecter.setOnClickListener(v -> {
            datePicker();
        });

        //onClick for search Button
        searchButton.setOnClickListener(v->{
            if(TextUtils.isEmpty(lsrc.getText().toString()) || lsrc.getText().toString().equals(""))
                lsrc.setError("Please Enter a source station");

            else if(TextUtils.isEmpty(ldest.getText().toString()) || ldest.getText().toString().equals(""))
                ldest.setError("Please Enter a destination station");
            else if(lsrc.getText().toString().equals(ldest.getText().toString()))
                ldest.setError("Source and destination can't  be same");

            else {

                source = lsrc.getText().toString();
                destination = ldest.getText().toString();
                Intent intent = new Intent(this,TrainResponseActivity.class);
                startActivity(intent);
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

        navigationView = findViewById(R.id.nav_view);
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
                    (SearchResultListener<Stations>) (dialog, item, position) -> {
                       lsrc.setText(item.getTitle().toString());
                        dialog.dismiss();
                    }).show();
        });
        ldest.setOnClickListener(v->{
            new SimpleSearchDialogCompat(this, "Search...",
                    "Search for Destination Staion", null, stationList(listOfStation),
                    (SearchResultListener<Stations>) (dialog, item, position) -> {
                        ldest.setText(item.getTitle().toString());
                        dialog.dismiss();
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

    //array for staions
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

    //search box for stations
    private ArrayList<Stations> stationList(ArrayList<String> stn){
        ArrayList<Stations> list = new ArrayList<>();

        for (int i =0; i < stn.size();i++)
            list.add(new Stations(stn.get(i)));

        return list;
    }

    //logout function
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        TrainSearchActivity.searchActivity.finish();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    //pop up logout confirmatio dialog
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

    //gets user name and details
    public void getUsername(){
        View view = navigationView.getHeaderView(0);
        TextView username = view.findViewById(R.id.username);
        ImageView imageView = view.findViewById(R.id.imageView);
        LinearLayout profileClick =view.findViewById(R.id.profileClick);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
           Log.v("Username",name);
            getImage(user.getUid(),imageView);
            profileClick.setOnClickListener(v->{
                startActivity(new Intent(this,ProfileActivity.class));
            });

            username.setText("Welcome\n" + name);
        } else {
            username.setText("No user name");
        }
    }

    //get Image
    public void getImage(String uid,ImageView imageView){
        StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference(uid+"/profile.jpg");
        final long ONE_MEGABYTE = 1024 * 1024*10;
        mImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);

                    imageView.setMinimumHeight(dm.heightPixels);
                    imageView.setMinimumWidth(dm.widthPixels);
                    imageView.setImageBitmap(bm);

                })

                .addOnFailureListener(exception -> {
                    // Handle any errors
                    imageView.setImageResource(R.drawable.engine);
                });

    }

    //date picker
    public void datePicker(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {


            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            cal.set(Calendar.YEAR, year);
            java.util.Date myDate = cal.getTime();
            date=new SimpleDateFormat("dd-MMM-yyyy").format(myDate);
            dateSelecter.setText(date);
            day=getDayofWeek(cal);
            Toast.makeText(searchActivity, "Day:"+day, Toast.LENGTH_SHORT).show();


        },mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();

    }

    //getting day for seaching trains on selected Date
    public String getDayofWeek(Calendar calendar){
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
        java.util.Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }
}
