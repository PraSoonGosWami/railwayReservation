package com.invaderx.railway;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;


import com.invaderx.railway.auth.ProfileActivity;
import com.rupins.drawercardbehaviour.CardDrawerLayout;

public class TrainSearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button searchButton;
    Toolbar toolbar;
    private CardDrawerLayout drawer;
    public static String source;
    public static String destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_search);
        EditText lsrc,ldest;
        lsrc=findViewById(R.id.lsrc);
        ldest=findViewById(R.id.ldesc);
        searchButton=findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v->{
            if(TextUtils.isEmpty(lsrc.getText().toString()) || lsrc.getText().toString().equals(""))
                lsrc.setError("Please Enter a source station");

            else if(TextUtils.isEmpty(ldest.getText().toString()) || ldest.getText().toString().equals(""))
                lsrc.setError("Please Enter a destination station");
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
            case R.id.nav_profile:
                startActivity(new Intent(this,ProfileActivity.class));
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //startActivity(new Intent(this,CalculatorActivity.class));
        return super.onOptionsItemSelected(item);

    }
}
