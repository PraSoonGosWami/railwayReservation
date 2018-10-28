package com.invaderx.railway.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.invaderx.railway.R;
import com.invaderx.railway.auth.LoginActivity;


public class TrainAddActivity extends AppCompatActivity {
    CheckBox ma1;
    CheckBox ma2;
    CheckBox ma3;
    CheckBox msl;
    CheckBox mcc;

    LinearLayout mLa1;
    LinearLayout mLa2;
    LinearLayout mLa3;
    LinearLayout mLsl;
    LinearLayout mLcc;
    EditText trainNumber;
    EditText trainName;
    EditText sourcePlace;
    EditText sourceTime;
    EditText destPlace;
    EditText destTime;
    EditText oneASeat;
    EditText oneAFair;
    EditText twoASeat;
    EditText twoAFair;
    EditText threeASeat;
    EditText threeAFair;
    EditText slSeat;
    EditText slFair;
    EditText ccSeat;
    EditText ccFair;
    String addTrain = null;
    Button trainSaveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_add);
        //for getting intent data and updating title
        Intent intent = getIntent();
        addTrain = intent.getExtras().getString("add");
        if (addTrain.equals("add")) {
            setTitle("Add Train");

            invalidateOptionsMenu();
        } else {
            setTitle("Update Train");

        }
        //end
        trainName = findViewById(R.id.train_name);
        trainNumber = findViewById(R.id.train_number);
        sourcePlace = findViewById(R.id.source_place);
        sourceTime = findViewById(R.id.source_time);
        destPlace = findViewById(R.id.dest_place);
        destTime = findViewById(R.id.dest_time);
        oneAFair = findViewById(R.id.one_a_fair);
        oneASeat = findViewById(R.id.one_a_seat);
        twoAFair = findViewById(R.id.two_a_fair);
        twoASeat = findViewById(R.id.two_a_seat);
        threeAFair = findViewById(R.id.three_a_fair);
        threeASeat = findViewById(R.id.three_a_seat);
        slFair = findViewById(R.id.sl_fair);
        slSeat = findViewById(R.id.sl_seat);
        ccFair = findViewById(R.id.cc_fair);
        ccSeat = findViewById(R.id.cc_seat);
        trainSaveButton = findViewById(R.id.train_save_button);
        ma1 = (CheckBox) findViewById(R.id.a1);
        ma2 = (CheckBox) findViewById(R.id.a2);
        ma3 = (CheckBox) findViewById(R.id.a3);
        msl = (CheckBox) findViewById(R.id.sl);
        mcc = (CheckBox) findViewById(R.id.cc);

        mLa1 = (LinearLayout) findViewById(R.id.a1linearlayout);
        mLa2 = (LinearLayout) findViewById(R.id.a2linearlayout);
        mLa3 = (LinearLayout) findViewById(R.id.a3linearlayout);
        mLsl = (LinearLayout) findViewById(R.id.sllinearlayout);
        mLcc = (LinearLayout) findViewById(R.id.cclinearlayout);

        ma1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ma1.isChecked()) {
                    mLa1.setVisibility(View.VISIBLE);
                } else {
                    mLa1.setVisibility(View.GONE);
                }
            }
        });
        ma2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ma2.isChecked()) {
                    mLa2.setVisibility(View.VISIBLE);
                } else {
                    mLa2.setVisibility(View.GONE);
                }
            }
        });
        ma3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ma3.isChecked()) {
                    mLa3.setVisibility(View.VISIBLE);
                } else {
                    mLa3.setVisibility(View.GONE);
                }
            }
        });
        msl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (msl.isChecked()) {
                    mLsl.setVisibility(View.VISIBLE);
                } else {
                    mLsl.setVisibility(View.GONE);
                }
            }
        });
        mcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mcc.isChecked()) {
                    mLcc.setVisibility(View.VISIBLE);
                } else {
                    mLcc.setVisibility(View.GONE);
                }
            }
        });

        trainSaveButton.setOnClickListener(view -> saveButton());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addtrain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_train) {
            //Add delete function here
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (addTrain.equals("add")) {
            MenuItem menuItem = menu.findItem(R.id.delete_train);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void saveButton() {
        String strainName = trainName.getText().toString().trim();
        String strainNumber = trainNumber.getText().toString().trim();
        String ssourcePlace = sourcePlace.getText().toString().trim();
        String ssourceTime = sourceTime.getText().toString().trim();
        String sdestPLace = destPlace.getText().toString().trim();
        String sdestTime = destTime.getText().toString().trim();

        if (strainName.isEmpty()) {
            trainName.setError("Train Name is required");
            trainName.requestFocus();
            return;
        }
        if (strainNumber.isEmpty()) {
            trainNumber.setError("Train Number is required");
            trainNumber.requestFocus();
            return;
        }
        if (ssourcePlace.isEmpty()) {
            sourcePlace.setError("Source Place is required");
            sourcePlace.requestFocus();
            return;
        }
        if (ssourceTime.isEmpty()) {
            sourceTime.setError("Source Time is required");
            sourceTime.requestFocus();
            return;
        }
        if (sdestPLace.isEmpty()) {
            destPlace.setError("Destination PLace is required");
            destPlace.requestFocus();
            return;
        }
        if (sdestTime.isEmpty()) {
            destTime.setError("Destination Time is required");
            destTime.requestFocus();
            return;
        }

    }

}
