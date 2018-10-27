package com.invaderx.railway.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.invaderx.railway.R;
import com.invaderx.railway.auth.LoginActivity;

public class TrainDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_details);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.admin_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(TrainDetailsActivity.this, LoginActivity.class));
        }

        return true;
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }*/
}
